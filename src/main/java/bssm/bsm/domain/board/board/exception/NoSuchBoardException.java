package bssm.bsm.domain.board.board.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchBoardException extends NotFoundException {
    public NoSuchBoardException() {
        super("게시판을 찾을 수 없습니다.");
    }
}
