package bssm.bsm.global.auth.exception;

import bssm.bsm.global.error.exceptions.UnAuthorizedException;

public class ExpiredAuthTokenException extends UnAuthorizedException {
    public ExpiredAuthTokenException() {
        super("로그인 정보가 만료되었습니다");
    }
}