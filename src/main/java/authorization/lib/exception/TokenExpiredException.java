package authorization.lib.exception;

import authorization.lib.constant.ErrorCode;

public class TokenExpiredException extends AuthException {
    public TokenExpiredException(String subject) {
        super("Token expired for subject: " + subject, ErrorCode.TOKEN_EXPIRED);
    }
}
