package bssm.bsm.domain.board.comment.exception;

import bssm.bsm.global.error.exceptions.ForbiddenException;

public class DoNotHavePermissionToUpdateCommentException extends ForbiddenException {
    public DoNotHavePermissionToUpdateCommentException() {
        super("이 댓글을 수정할 권한이 없습니다.");
    }
}
