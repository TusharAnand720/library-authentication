package authentication.lib.service;

import authentication.lib.exception.InvalidTokenException;
import authentication.lib.exception.SigningException;
import authentication.lib.exception.TokenExpiredException;
import authentication.lib.model.AuthToken;
import authentication.lib.model.JwtClaims;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    /**
     * @param userId
     * @return {@link AuthToken}
     * @throws SigningException
     **/
    AuthToken issueToken(String userId);

    /**
     *
     * @return {@link JwtClaims}
     * @throws SigningException
     * @throws InvalidTokenException
     * @throws TokenExpiredException
     **/
    JwtClaims validateToken(HttpServletRequest servletRequest);

    /**
     *
     * @return {@link JwtClaims}
     * @throws SigningException
     * @throws InvalidTokenException
     * @throws TokenExpiredException
     **/
    JwtClaims validateToken(String rawToken);
}
