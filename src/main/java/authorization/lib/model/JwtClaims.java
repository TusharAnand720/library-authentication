package authorization.lib.model;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class JwtClaims {

    private final String subject;
    private final List<String> roles;
    private final Instant issuedAt;
    private final Instant expiry;
    private final String issuer;

    // used when GENERATING a token
    public JwtClaims(String subject, List<String> roles) {
        this.subject = subject;
        this.roles = Collections.unmodifiableList(roles);
        this.issuedAt = null;   // JwtTokenServiceImpl fills these during signing
        this.expiry = null;
        this.issuer = null;
    }

    public JwtClaims(String subject) {
        this.subject = subject;
        this.roles = null;
        this.issuedAt = null;   // JwtTokenServiceImpl fills these during signing
        this.expiry = null;
        this.issuer = null;
    }

    // used when PARSING a token
    public JwtClaims(String subject, List<String> roles, Instant issuedAt, Instant expiry, String issuer) {
        this.subject = subject;
        this.roles = Collections.unmodifiableList(roles);
        this.issuedAt = issuedAt;
        this.expiry = expiry;
        this.issuer = issuer;
    }

//    public boolean hasRole(String role) {
//        return roles.contains(role);
//    }

    // checks if token is expired based on current time and expiry claim
    public boolean isExpired() {
        if (expiry == null) return false;
        return Instant.now().isAfter(expiry);
    }

    public String getSubject() {
        return subject;
    }

    public List<String> getRoles() {
        return roles;
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

}
