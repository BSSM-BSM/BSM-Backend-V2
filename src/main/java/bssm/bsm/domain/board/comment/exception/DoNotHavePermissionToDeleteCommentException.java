package bssm.bsm.domain.board.comment.exception;

import bssm.bsm.global.error.exceptions.ForbiddenException;

public class DoNotHavePermissionToDeleteCommentException extends ForbiddenException {
    public DoNotHavePermissionToDeleteCommentException() {
        super("이 댓글을 삭제할 권한이 없습니다.");
    }
}
