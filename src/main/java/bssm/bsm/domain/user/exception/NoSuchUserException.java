package bssm.bsm.domain.user.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchUserException extends NotFoundException {
    public NoSuchUserException() {
        super("유저를 찾을 수 없습니다.");
    }
}