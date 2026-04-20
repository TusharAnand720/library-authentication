package authorization.lib.exception;

import authorization.lib.constant.ErrorCode;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException(String message) {
        super("Invalid Token : " + message, ErrorCode.TOKEN_INVALID);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super("Invalid Token : " + message, ErrorCode.TOKEN_INVALID, cause);
    }
}
