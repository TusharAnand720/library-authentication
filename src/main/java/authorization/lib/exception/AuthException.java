package authorization.lib.exception;

public class AuthException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public enum ErrorCode {
        TOKEN_EXPIRED,
        TOKEN_INVALID,
        TOKEN_MISSING,
        SIGNING_FAILURE
    }
}
