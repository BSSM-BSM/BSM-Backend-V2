package bssm.bsm.domain.auth.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchBsmAuthCodeException extends NotFoundException {
    public NoSuchBsmAuthCodeException() {
        super("BSM OAuth 인증 코드를 찾을 수 없습니다.");
    }
}