package authorization.lib.middleware;

import authorization.lib.model.AuthToken;
import authorization.lib.model.JwtClaims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AuthOperations {

    AuthToken issueToken(String userId, List<String> roles);

    AuthToken issueToken(String userId);

    JwtClaims authenticateRequest(HttpServletRequest request);

    JwtClaims validateToken(String rawToken);

    boolean isTokenExpired(String rawToken);

    JwtClaims authenticatedUser();


}
