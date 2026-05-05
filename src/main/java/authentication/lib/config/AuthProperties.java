package authentication.lib.config;

import authentication.lib.constant.Algorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "auth.jwt")
public class AuthProperties {

    private final String secret;
    private final String issuer;
    private final long expirySeconds;
    private final Algorithm algorithm;

    @ConstructorBinding
    public AuthProperties(String secret, String issuer, long expirySeconds, Algorithm algorithm) {

        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("auth.jwt.secret must be set");
        }
        if (secret.getBytes().length < 32) {
            throw new IllegalArgumentException("auth.jwt.secret must be at least 32 bytes");
        }

        this.secret = secret;
        this.issuer = issuer != null ? issuer : "auth-lib";
        this.expirySeconds = expirySeconds > 0 ? expirySeconds : 3600L;
        this.algorithm = algorithm != null ? algorithm : Algorithm.HS256;
    }

    public String getSecret() {
        return secret;
    }

    public String getIssuer() {
        return issuer;
    }

    public long getExpirySeconds() {
        return expirySeconds;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
