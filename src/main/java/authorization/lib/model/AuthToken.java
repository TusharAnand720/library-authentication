package authorization.lib.model;

import java.time.Instant;

// Response class for token
public class AuthToken {
    private final String rawToken;
    private final Instant issuedAt;
    private final Instant expiry;

    public AuthToken(String rawToken, Instant issuedAt, Instant expiry) {
        this.rawToken = rawToken;
        this.issuedAt = issuedAt;
        this.expiry = expiry;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiry);
    }

    public long expiresInSeconds() {
        long seconds = expiry.getEpochSecond() - Instant.now().getEpochSecond();
        return Math.max(0, seconds);    // never return negative
    }

    public String getRawToken() {
        return rawToken;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public Instant getExpiry() {
        return expiry;
    }

}
