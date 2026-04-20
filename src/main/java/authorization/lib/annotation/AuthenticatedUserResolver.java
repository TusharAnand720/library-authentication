package authorization.lib.annotation;

import authorization.lib.context.AuthContext;
import authorization.lib.exception.TokenMissingException;
import authorization.lib.model.JwtClaims;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticatedUserResolver implements HandlerMethodArgumentResolver {

    // tells Spring which parameters this resolver handles
    // Parameters annotated with @AuthenticatedUser AND of type JwtClaims
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUser.class)
                && parameter.getParameterType().equals(JwtClaims.class);
    }


    // called by Spring when supportsParameter() returns true
    // Pulls the claims from AuthContext and injects them into the parameter
    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter,
                                            @Nullable ModelAndViewContainer mavContainer,
                                            NativeWebRequest webRequest,
                                            @Nullable WebDataBinderFactory binderFactory) throws Exception {
        JwtClaims claims = AuthContext.getCurrentUser();

        if (claims == null) {
            throw new TokenMissingException();
        }

        return claims;
    }
}
