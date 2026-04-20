package authorization.lib.store;

import authorization.lib.model.JwtClaims;

public interface TokenStore {

    /*
    Checks is token is revoked or not
    In V1 this will always return false
    In V2 it will use cache to check if given token is revoked or not
     */
    boolean isRevoked(String rawToken);

    /*
    This will be used to revoke the token
    In V1 there will be no implementation but in V2 it will mark the token as revoked in cache
     */
    void revoke(String rawToken, JwtClaims claims);

    /*
    Will revoke all the tokens based on the algo and signature provided
     */
    void revokeAll();
}
