package authorization.lib.context;

import authorization.lib.model.JwtClaims;

public class AuthContext {

    // using ThreadLocal for one instance of JwtClaims per thread — never shared across threads
    private static final ThreadLocal<JwtClaims> store = new ThreadLocal<>();

    private AuthContext() {
    }

    public static void set(JwtClaims claims) {
        if (claims == null) {
            throw new IllegalArgumentException("claims must not be null");
        }
        store.set(claims);
    }

    public static JwtClaims getCurrentUser() {
        return store.get();
    }

    public static boolean isAuthenticated() {
        return store.get() != null;
    }

//    public static boolean hasRole(String role) {
//        JwtClaims claims = store.get();
//        return claims != null && claims.hasRole(role);
//    }

    public static void clear() {
        store.remove();
    }
}
