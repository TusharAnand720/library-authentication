package authorization.lib.service;

import authorization.lib.model.AuthToken;
import authorization.lib.model.JwtClaims;

public interface AuthService {

    /**
     * @param userId
     * @return {@link AuthToken}
     * @throws authorization.lib.exception.SigningException
     **/
    AuthToken issueToken(String userId);

    /**
     *
     * @param rawToken
     * @return {@link JwtClaims}
     * @throws authorization.lib.exception.SigningException
     * @throws authorization.lib.exception.InvalidTokenException
     * @throws authorization.lib.exception.TokenExpiredException
     **/
    JwtClaims validateToken(String rawToken);
}
