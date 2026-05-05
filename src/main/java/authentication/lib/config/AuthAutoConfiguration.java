package authentication.lib.config;

import authentication.lib.service.AuthService;
import authentication.lib.service.AuthServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
@ConditionalOnProperty(prefix = "auth.jwt", name = "secret")
public class AuthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthService.class)
    public AuthService authService(AuthProperties properties) {
        return new AuthServiceImpl(properties);
    }

    @Bean
    public AuthChannelInterceptor authChannelInterceptor(AuthService authService) {
        // AuthService found via @ComponentScan above, injected here
        return new AuthChannelInterceptor(authService);
    }
}
