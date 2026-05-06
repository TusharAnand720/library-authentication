package authentication.lib.model;

import java.security.Principal;
import java.time.Instant;

public class JwtClaims implements Principal {

    private final String subject;
    private final Instant issuedAt;
    private final Instant expiry;
    private final String issuer;

    public JwtClaims(String subject) {
        this.subject = subject;
        this.issuedAt = null;
        this.expiry = null;
        this.issuer = null;
    }

    public JwtClaims(String subject, Instant issuedAt, Instant expiry, String issuer) {
        this.subject = subject;
        this.issuedAt = issuedAt;
        this.expiry = expiry;
        this.issuer = issuer;
    }

    public boolean isExpired() {
        if (expiry == null) return false;
        return Instant.now().isAfter(expiry);
    }

    public String getSubject() {
        return subject;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public Instant getExpiry() {
        return expiry;
    }

    public String getIssuer() {
        return issuer;
    }

    @Override
    public String getName() {
        return this.subject;
    }
}
