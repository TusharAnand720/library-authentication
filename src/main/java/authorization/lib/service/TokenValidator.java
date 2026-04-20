package authorization.lib.service;

import authorization.lib.model.JwtClaims;

public interface TokenValidator {
    /**
     * Parse and validate an incoming raw JWT string.
     * Verifies signature, expiry, issuer, and revocation status.
     *
     * @throws authorization.lib.exception.TokenExpiredException if token is Expired                                            if token has expired
     * @throws authorization.lib.exception.InvalidTokenException if token is malformed or signature fails
     * @throws authorization.lib.exception.SigningException      if key material cannot be loaded
     */
    JwtClaims validate(String rawToken);

    /**
     * Lightweight check — does not throw, just returns true/false.
     */
    boolean isExpired(String rawToken);

    /**
     * Extract claims without validating signature or expiry.
     */
    JwtClaims extractUnchecked(String rawToken);
}
