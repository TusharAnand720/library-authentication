package authentication.lib.config;

import authentication.lib.exception.InvalidTokenException;
import authentication.lib.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

public class WebSocketAuthConfig implements WebSocketMessageBrokerConfigurer {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private AuthService authService;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Validate JWT on CONNECT
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    try {
                        String header = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);
                        if (header == null || !header.startsWith(BEARER_PREFIX)) {
                            throw new InvalidTokenException("invalid token Header not founds");
                        }

                        String rawToken = header.substring(BEARER_PREFIX.length()).strip();
                        if (rawToken.isBlank()) {
                            throw new InvalidTokenException("token must not be null or blank");
                        }
                        authService.validateToken(rawToken);
                    } catch (Exception e) {
                        throw new MessagingException(e.getMessage());
                    }
                }
                return message;
            }
        });
    }
}
