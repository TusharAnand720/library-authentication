package authorization.lib.exception;

public class TokenMissingException extends AuthException {
    public TokenMissingException() {
        super("Authorization header missing or malformed", ErrorCode.TOKEN_MISSING);
    }
}
