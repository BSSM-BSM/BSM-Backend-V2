package bssm.bsm.domain.board.board.exception;

import bssm.bsm.global.error.exceptions.ForbiddenException;

public class StudentAccessOnlyException extends ForbiddenException {
    public StudentAccessOnlyException() {
        super("학생만 접근할 수 있는 게시판입니다");
    }
}
