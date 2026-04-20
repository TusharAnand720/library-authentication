package authorization.lib.exception;

public class SigningException extends AuthException {

    public SigningException(String reason, Throwable cause) {
        super("Token signing failed: " + reason, ErrorCode.SIGNING_FAILURE, cause);
    }
}
