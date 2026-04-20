package authorization.lib.config;

import authorization.lib.annotation.AuthenticatedUserResolver;
import authorization.lib.filter.JwtAuthFilter;
import authorization.lib.service.impl.JwtTokenServiceImpl;
import authorization.lib.store.NoOpTokenStore;
import authorization.lib.store.TokenStore;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

// will load the AutoConfiguration.imports file rather than component scanning
@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
// this will make sure that auth.jwt.secret is set in property file , if not the entire configuration will be skipped
@ConditionalOnProperty(prefix = "auth.jwt", name = "secret")
public class AuthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    /*
     ConditionalOnMissingBean is used to register a bean only if no other bean of specific name
     or type already exists if any consuming service using this lib overrides or define their own class then
     spring will skip the given class and service class will override the lib class
     */
    public JwtConfig jwtConfig(AuthProperties authProperties) {
        return switch (authProperties.getAlgorithm()) {
            case HS256 -> new JwtConfig(
                    authProperties.getSecret(),
                    authProperties.getExpirySeconds(),
                    authProperties.getIssuer()
            );
            case RS256 -> new JwtConfig(
                    authProperties.getPrivateKeyPath(),
                    authProperties.getPublicKeyPath(),
                    authProperties.getExpirySeconds(),
                    authProperties.getIssuer()
            );
        };
    }

    @Bean
    @ConditionalOnMissingBean(TokenStore.class)
    public TokenStore tokenStore() {
        return new NoOpTokenStore();
    }

    @Bean
    @ConditionalOnMissingBean(JwtTokenServiceImpl.class)
    public JwtTokenServiceImpl jwtTokenService(JwtConfig config, TokenStore tokenStore) {
        return new JwtTokenServiceImpl(config, tokenStore);
    }

    @Bean
    @ConditionalOnMissingBean(JwtAuthFilter.class)
    public JwtAuthFilter jwtAuthFilter(JwtTokenServiceImpl jwtTokenService,
                                       ObjectMapper objectMapper) {
        return new JwtAuthFilter(jwtTokenService, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticatedUserResolver.class)
    public AuthenticatedUserResolver authenticatedUserResolver() {
        return new AuthenticatedUserResolver();
    }

    // Register the anno
    @Bean
    @ConditionalOnMissingBean(WebMvcConfigurer.class)
    public WebMvcConfigurer authWebMvcConfigurer(AuthenticatedUserResolver resolver) {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(resolver);
            }
        };
    }
}
