package bssm.bsm.domain.board.comment.exception;

import bssm.bsm.global.error.exceptions.ForbiddenException;

public class DoNotHavePermissionToWriteCommentOnBoardException extends ForbiddenException {
    public DoNotHavePermissionToWriteCommentOnBoardException() {
        super("이 게시판에 댓글을 작성할 권한이 없습니다.");
    }
}
