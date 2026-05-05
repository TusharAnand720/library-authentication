package authentication.lib.exception;

import authentication.lib.constant.ErrorCode;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException(String reason) {
        super("Invalid token: " + reason, ErrorCode.TOKEN_INVALID);
    }

    public InvalidTokenException(String reason, Throwable cause) {
        super("Invalid token: " + reason, ErrorCode.TOKEN_INVALID, cause);
    }
}
