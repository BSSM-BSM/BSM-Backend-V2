package bssm.bsm.domain.board.comment.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchCommentException extends NotFoundException {
    public NoSuchCommentException() {
        super("댓글을 찾을 수 없습니다.");
    }
}