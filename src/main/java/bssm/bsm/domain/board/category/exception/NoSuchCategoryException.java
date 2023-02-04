package bssm.bsm.domain.board.category.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchCategoryException extends NotFoundException {
    public NoSuchCategoryException() {
        super("카테고리를 찾을 수 없습니다.");
    }
}
