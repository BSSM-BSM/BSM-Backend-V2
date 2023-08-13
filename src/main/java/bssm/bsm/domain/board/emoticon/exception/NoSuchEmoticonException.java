package bssm.bsm.domain.board.emoticon.exception;

import bssm.bsm.global.error.exceptions.NotFoundException;

public class NoSuchEmoticonException extends NotFoundException {
    public NoSuchEmoticonException() {
        super("이모티콘을 찾을 수 없습니다.");
    }
}
