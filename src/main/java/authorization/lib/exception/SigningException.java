package authorization.lib.exception;

import authorization.lib.constant.ErrorCode;

public class SigningException extends AuthException {

    public SigningException(String reason, Throwable cause) {
        super("Token signing failed: " + reason, ErrorCode.SIGNING_FAILURE, cause);
    }
}
