package authentication.lib.config;

import authentication.lib.exception.InvalidTokenException;
import authentication.lib.service.AuthService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class AuthChannelInterceptor implements ChannelInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthService authService;

    public AuthChannelInterceptor(AuthService authService) {
        this.authService = authService;
    }
    
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        System.out.println("WebSocketAuthConfig: " + accessor.getCommand());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            try {
                System.out.println("Validating Authorization Token for WebSocket");
                String header = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);

                if (header == null || !header.startsWith(BEARER_PREFIX)) {
                    throw new InvalidTokenException("invalid token header not found");
                }

                String rawToken = header.substring(BEARER_PREFIX.length()).strip();
                if (rawToken.isBlank()) {
                    throw new InvalidTokenException("token must not be null or blank");
                }

                authService.validateToken(rawToken); // ✅ your existing logic unchanged

            } catch (Exception e) {
                System.err.println(e.getMessage());
                throw new MessagingException(e.getMessage());
            }
        }
        return message;
    }

}
