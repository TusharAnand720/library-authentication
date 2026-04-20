package authorization.lib.exception;

import authorization.lib.constant.ErrorCode;

public class TokenExpiredException extends AuthException {

    public TokenExpiredException(String message) {
        super("Token Expired : " + message, ErrorCode.TOKEN_EXPIRED);
    }
}
