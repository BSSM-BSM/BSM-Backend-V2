package bssm.bsm.domain.board.lostfound.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchLostFoundException extends NotFoundException {
    public NoSuchLostFoundException() {
        super("분실물 페이지를 찾을 수 없습니다.");
    }
}
