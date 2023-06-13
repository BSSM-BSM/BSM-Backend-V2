package bssm.bsm.domain.board.lostfound.exception;

import bssm.bsm.global.error.exceptions.ForbiddenException;

public class NotCreatorException extends ForbiddenException {
    public NotCreatorException() {
        super("글을 작성한 사용자가 아닙니다.");
    }
}
