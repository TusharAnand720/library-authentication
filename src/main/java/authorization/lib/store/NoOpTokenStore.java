package authorization.lib.store;

import authorization.lib.model.JwtClaims;

/*
This class will not perform any action in V1 but in V2 this will be used to perform revoke actions with cache
 */
public class NoOpTokenStore implements TokenStore {

    @Override
    public boolean isRevoked(String rawToken) {
        return false;
    }

    @Override
    public void revoke(String rawToken, JwtClaims claims) {

    }

    @Override
    public void revokeAll() {

    }
}
