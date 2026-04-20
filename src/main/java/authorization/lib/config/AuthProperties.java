package authorization.lib.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "auth.jwt")
public class AuthProperties {

    private final String secret;
    private final String privateKeyPath;
    private final String publicKeyPath;
    private final Algorithm algorithm;
    private final long expirySeconds;
    private final String issuer;

    @ConstructorBinding
    public AuthProperties(String secret, String privateKeyPath, String publicKeyPath, Algorithm algorithm, long expirySeconds, String issuer) {
        this.algorithm = algorithm != null ? algorithm : Algorithm.HS256; // default algo will be HS256
        this.expirySeconds = expirySeconds > 0 ? expirySeconds : 1800L; // if expirySeconds is not provided then it will be of 30 mins
        this.issuer = issuer;
        this.secret = secret;
        this.privateKeyPath = privateKeyPath;
        this.publicKeyPath = publicKeyPath;
    }

    public String getSecret() {
        return secret;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public long getExpirySeconds() {
        return expirySeconds;
    }

    public String getIssuer() {
        return issuer;
    }
}
