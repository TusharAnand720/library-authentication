package authorization.lib.config;

public class JwtConfig {

    private String secret;
    private String privateKeyPath;
    private String publicKeyPath;
    private Algorithm algorithm;
    private long expirySeconds;
    private String issuer;

    // constructor for HS256
    public JwtConfig(String secret, long expirySeconds, String issuer) {
        this.secret = secret;
        this.algorithm = Algorithm.HS256;
        this.expirySeconds = expirySeconds;
        this.issuer = issuer;
    }

    // constructor for RS256
    public JwtConfig(String privateKeyPath, String publicKeyPath, long expirySeconds, String issuer) {
        this.privateKeyPath = privateKeyPath;
        this.publicKeyPath = publicKeyPath;
        this.algorithm = Algorithm.RS256;
        this.expirySeconds = expirySeconds;
        this.issuer = issuer;
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
