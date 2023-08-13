package bssm.bsm.domain.board.post.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchPostException extends NotFoundException {
    public NoSuchPostException() {
        super("게시글을 찾을 수 없습니다.");
    }
}