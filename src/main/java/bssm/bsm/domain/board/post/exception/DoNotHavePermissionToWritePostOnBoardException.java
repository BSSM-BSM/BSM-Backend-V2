package bssm.bsm.domain.board.post.exception;

import bssm.bsm.global.error.exceptions.ForbiddenException;

public class DoNotHavePermissionToWritePostOnBoardException extends ForbiddenException {
    public DoNotHavePermissionToWritePostOnBoardException() {
        super("이 게시판에 글을 작성할 권한이 없습니다.");
    }
}
