package bssm.bsm.domain.auth.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchRefreshTokenException extends NotFoundException {
    public NoSuchRefreshTokenException() {
        super("학생을 찾을 수 없습니다.");
    }
}