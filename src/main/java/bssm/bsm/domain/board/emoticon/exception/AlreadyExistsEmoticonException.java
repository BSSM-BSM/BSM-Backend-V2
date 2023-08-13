package bssm.bsm.domain.board.emoticon.exception;

import bssm.bsm.global.error.exceptions.ConflictException;

public class AlreadyExistsEmoticonException extends ConflictException {
    public AlreadyExistsEmoticonException() {
        super("해당 이름의 이모티콘이 이미 존재합니다");
    }
}
