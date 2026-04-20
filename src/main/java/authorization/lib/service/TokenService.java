package authorization.lib.service;

import authorization.lib.model.AuthToken;
import authorization.lib.model.JwtClaims;

public interface TokenService {
    /**
     * Sign and generate a JWT from the given claims.
     * Called once at login — returns the token to hand back to the client.
     *
     * @throws authorization.lib.exception.SigningException if key material is invalid
     */
    AuthToken generate(JwtClaims claims);
}
