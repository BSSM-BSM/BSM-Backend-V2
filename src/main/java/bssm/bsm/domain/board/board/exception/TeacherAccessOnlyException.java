package bssm.bsm.domain.board.board.exception;

import bssm.bsm.global.error.exceptions.ForbiddenException;

public class TeacherAccessOnlyException extends ForbiddenException {
    public TeacherAccessOnlyException() {
        super("선생님만 접근할 수 있는 게시판입니다");
    }
}
