package authorization.lib.config;

import authorization.lib.constant.Algorithm;

public class JwtConfig {

    private String secretKey;
    private Algorithm algorithm;
    private long expirationMillis;
    private String issuer;

    public JwtConfig(String secretKey, Algorithm algorithm, long expirationMillis, String issuer) {
        this.secretKey = secretKey;
        this.algorithm = algorithm;
        this.expirationMillis = expirationMillis;
        this.issuer = issuer;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public long getExpirationMillis() {
        return expirationMillis;
    }

    public void setExpirationMillis(long expirationMillis) {
        this.expirationMillis = expirationMillis;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
