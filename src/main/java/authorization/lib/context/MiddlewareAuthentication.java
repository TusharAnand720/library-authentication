package authorization.lib.context;

import authorization.lib.model.JwtClaims;

public interface MiddlewareAuthentication {

    void set(JwtClaims claims);

    boolean isAuthenticated();

    void clear();

    JwtClaims getCurrentUser();
}
