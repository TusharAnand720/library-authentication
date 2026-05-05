package authentication.lib.service;

import authentication.lib.config.AuthProperties;
import authentication.lib.exception.InvalidTokenException;
import authentication.lib.exception.SigningException;
import authentication.lib.exception.TokenExpiredException;
import authentication.lib.model.AuthToken;
import authentication.lib.model.JwtClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

public class AuthServiceImpl implements AuthService {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";


    private final AuthProperties properties;
    private final SecretKey signingKey;

    public AuthServiceImpl(AuthProperties properties) {
        this.properties = properties;
        this.signingKey = resolveSigningKey(properties.getSecret());
    }

    @Override
    public AuthToken issueToken(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be null or blank");
        }

        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(properties.getExpirySeconds());

        try {
            String raw = Jwts.builder()
                    .subject(userId)
                    .issuer(properties.getIssuer())
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(expiry))
                    .signWith(signingKey)
                    .compact();

            return new AuthToken(raw, now, expiry);

        } catch (Exception e) {
            throw new SigningException("could not sign token for: " + userId, e);
        }
    }

    @Override
    public JwtClaims validateToken(HttpServletRequest servletRequest) {
        String header = servletRequest.getHeader(AUTHORIZATION_HEADER);
        return validateToken(header);
    }

    public JwtClaims validateToken(String rawToken) {
        if (rawToken == null || !rawToken.startsWith(BEARER_PREFIX)) {
            throw new InvalidTokenException("invalid token Header not founds");
        }

        rawToken = rawToken.substring(BEARER_PREFIX.length()).strip();
        if (rawToken.isBlank()) {
            throw new InvalidTokenException("token must not be null or blank");
        }

        Claims body = parse(rawToken);
        return claimsFrom(body);
    }

    private Claims parse(String rawToken) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .requireIssuer(properties.getIssuer())
                    .build()
                    .parseSignedClaims(rawToken)
                    .getPayload();

        } catch (ExpiredJwtException e) {
            // extract subject from expired token so the error message is useful
            String subject = e.getClaims().getSubject();
            throw new TokenExpiredException(subject);

        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage(), e);
        }
    }

    private JwtClaims claimsFrom(Claims body) {
        return new JwtClaims(
                body.getSubject(),
                body.getIssuedAt().toInstant(),
                body.getExpiration().toInstant(),
                body.getIssuer()
        );
    }

    // resolves secret key in HS256
    private SecretKey resolveSigningKey(String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }
}
