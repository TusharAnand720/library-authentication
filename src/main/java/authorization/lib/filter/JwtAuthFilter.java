package authorization.lib.filter;

import authorization.lib.context.AuthContext;
import authorization.lib.exception.AuthException;
import authorization.lib.exception.TokenMissingException;
import authorization.lib.model.JwtClaims;
import authorization.lib.service.TokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

/*
    OncePerRequestFilter is an abstract base class used to ensure that a specific filter logic is executed
     only once per HTTP request
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenValidator validator;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(TokenValidator validator, ObjectMapper objectMapper) {
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader(AUTHORIZATION_HEADER);
        // if Authorization is missing let it pass and let the controller decide what to do
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String rawToken = header.substring(BEARER_PREFIX.length()).strip();
        // if Authorization is there but there is no token then return an error response with exception TokenMissingException
        if (rawToken.isEmpty()) {
            writeError(response, new TokenMissingException());
            return;
        }

        try {
            JwtClaims claims = validator.validate(rawToken);
            AuthContext.set(claims);
            chain.doFilter(request, response);
        } catch (AuthException e) {
            writeError(response, e);
        } finally {
            AuthContext.clear(); // always clears ThreadLocal<>
        }
    }

    // routes should not be filtered if they are in the permit list
    // API path with /auth/ || /actuator/health || /public/
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/")
                || path.startsWith("/actuator/health")
                || path.startsWith("/public/")
                || path.startsWith("/register");
    }

    // =========== WRITES UNAUTHORIZED(401) ERROR IN HttpServletResponse ===========
    private void writeError(HttpServletResponse response, AuthException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 UNAUTHORIZED
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = Map.of(
                "error", e.getErrorCode().name(),
                "message", e.getMessage(),
                "timestamp", Instant.now().toString()
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

//    private int resolveStatus(AuthException e) {
//        return switch (e.getErrorCode()) {
//            case TOKEN_EXPIRED,
//                 TOKEN_INVALID,
//                 TOKEN_MISSING,
//                 SIGNING_FAILURE -> HttpServletResponse.SC_UNAUTHORIZED;
//        };
//    }
}
