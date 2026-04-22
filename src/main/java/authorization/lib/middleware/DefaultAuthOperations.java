package authorization.lib.middleware;

import authorization.lib.context.AuthContext;
import authorization.lib.exception.TokenMissingException;
import authorization.lib.model.AuthToken;
import authorization.lib.model.JwtClaims;
import authorization.lib.service.TokenService;
import authorization.lib.service.TokenValidator;
import authorization.lib.store.TokenStore;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class DefaultAuthOperations implements AuthOperations {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final TokenValidator tokenValidator;
    private final TokenStore tokenStore;

    public DefaultAuthOperations(TokenService tokenService, TokenValidator tokenValidator, TokenStore tokenStore) {
        this.tokenService = tokenService;
        this.tokenValidator = tokenValidator;
        this.tokenStore = tokenStore;
    }

    @Override
    public AuthToken issueToken(String userId, List<String> roles) {
        JwtClaims claims = new JwtClaims(userId, roles);
        return tokenService.generate(claims);

    }

    @Override
    public AuthToken issueToken(String userId) {
        JwtClaims claims = new JwtClaims(userId);
        return tokenService.generate(claims);
    }

    @Override
    public JwtClaims authenticateRequest(HttpServletRequest request) {
        String rawToken = extractToken(request);
        return tokenValidator.validate(rawToken);
    }

    @Override
    public JwtClaims validateToken(String rawToken) {
        return tokenValidator.validate(rawToken);
    }

    @Override
    public boolean isTokenExpired(String rawToken) {
        return tokenValidator.isExpired(rawToken);
    }

    @Override
    public JwtClaims authenticatedUser() {
        JwtClaims claims = AuthContext.getCurrentUser();
        if (claims == null) {
            throw new TokenMissingException();
        }
        return claims;
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            throw new TokenMissingException();
        }

        String token = header.substring(BEARER_PREFIX.length()).strip();

        if (token.isEmpty()) {
            throw new TokenMissingException();
        }

        return token;
    }
}
