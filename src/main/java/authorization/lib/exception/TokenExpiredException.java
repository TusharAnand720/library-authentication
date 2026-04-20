package authorization.lib.exception;

public class TokenExpiredException extends AuthException {

    public TokenExpiredException(String message) {
        super("Token Expired : " + message, ErrorCode.TOKEN_EXPIRED);
    }
}
