package bssm.bsm.domain.user.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchStudentException extends NotFoundException {
    public NoSuchStudentException() {
        super("학생을 찾을 수 없습니다.");
    }
}