package authorization.lib.exception;

import authorization.lib.constant.ErrorCode;

public class TokenMissingException extends AuthException {
    public TokenMissingException() {
        super("Authorization header missing or malformed", ErrorCode.TOKEN_MISSING);
    }
}
