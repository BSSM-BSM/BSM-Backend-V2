package bssm.bsm.domain.board.post.exception;

import bssm.bsm.global.error.exceptions.ForbiddenException;

public class DoNotHavePermissionToModifyPostException extends ForbiddenException {
    public DoNotHavePermissionToModifyPostException() {
        super("이 글을 수정할 권한이 없습니다.");
    }
}
