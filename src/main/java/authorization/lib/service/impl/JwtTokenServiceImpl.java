package authorization.lib.service.impl;

import authorization.lib.config.Algorithm;
import authorization.lib.config.JwtConfig;
import authorization.lib.exception.InvalidTokenException;
import authorization.lib.exception.SigningException;
import authorization.lib.exception.TokenExpiredException;
import authorization.lib.model.AuthToken;
import authorization.lib.model.JwtClaims;
import authorization.lib.service.TokenService;
import authorization.lib.service.TokenValidator;
import authorization.lib.store.TokenStore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class JwtTokenServiceImpl implements TokenService, TokenValidator {

    private final JwtConfig config;
    private final TokenStore store;

    //
    private final SecretKey hmacKey; // HS256
    private final PrivateKey privateKey; // RS256 signing
    private final PublicKey publicKey; // RS256 verification

    public JwtTokenServiceImpl(JwtConfig config, TokenStore store) {
        this.config = config;
        this.store = store;
        this.hmacKey = config.getAlgorithm() == Algorithm.HS256 ? resolveHmacKey(config.getSecret()) : null;
        this.privateKey = config.getAlgorithm() == Algorithm.RS256 ? resolvePrivateKey(config.getPrivateKeyPath()) : null;
        this.publicKey = config.getAlgorithm() == Algorithm.RS256 ? resolvePublicKey(config.getPublicKeyPath()) : null;
    }

    // ======== TokenService Implementation ========

    @Override
    public AuthToken generate(JwtClaims claims) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(config.getExpirySeconds());
        try {
            String raw = Jwts.builder()
                    .subject(claims.getSubject())
                    .issuer(config.getIssuer())
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(expiry))
                    .signWith(signingKey())
                    .compact();
            return new AuthToken(raw, now, expiry);
        } catch (Exception e) {
            throw new SigningException("failed to sign token for: " + claims.getSubject(), e);
        }
    }

    // ======== TokenValidator Implementation ========

    @Override
    public JwtClaims validate(String rawToken) {
        Claims body = parse(rawToken);
        if (store.isRevoked(rawToken)) {
            throw new InvalidTokenException("token has been revoked");
        }
        return claimsFrom(body);
    }

    @Override
    public boolean isExpired(String rawToken) {
        try {
            parse(rawToken);
            return false;
        } catch (TokenExpiredException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public JwtClaims extractUnchecked(String rawToken) {
        return null;
    }

    // ================= Helper =================

    private Claims parse(String rawToken) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) verificationKey())
                    .requireIssuer(config.getIssuer())
                    .build()
                    .parseSignedClaims(rawToken)
                    .getPayload();

        } catch (ExpiredJwtException e) {
            String subject = e.getClaims().getSubject();
            throw new TokenExpiredException(subject);

        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage(), e);
        }
    }

    private JwtClaims claimsFrom(Claims body) {
        List<String> roles = body.get("roles", List.class);
        return new JwtClaims(
                body.getSubject(),
                roles != null ? roles : List.of(),
                body.getIssuedAt().toInstant(),
                body.getExpiration().toInstant(),
                body.getIssuer()
        );
    }

    public Key signingKey() {
        return switch (config.getAlgorithm()) {
            case HS256 -> hmacKey;
            case RS256 -> privateKey;
        };
    }

    private Key verificationKey() {
        return config.getAlgorithm() == Algorithm.HS256 ? hmacKey : publicKey;
    }

    // ================= Key Resolution =================

    private SecretKey resolveHmacKey(String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalArgumentException("HS256 secret must be at least 32 bytes");
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    private PrivateKey resolvePrivateKey(String path) {
        try {
            String pem = Files.readString(Paths.get(path))
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(pem);
            return KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        } catch (Exception e) {
            throw new SigningException("could not load private key from: " + path, e);
        }
    }

    private PublicKey resolvePublicKey(String path) {
        try {
            String pem = Files.readString(Paths.get(path))
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(pem);
            return KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decoded));
        } catch (Exception e) {
            throw new SigningException("could not load public key from: " + path, e);
        }
    }
}
