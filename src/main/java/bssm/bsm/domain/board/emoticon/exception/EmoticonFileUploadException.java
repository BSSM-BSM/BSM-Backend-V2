package bssm.bsm.domain.board.emoticon.exception;

import bssm.bsm.global.error.exceptions.InternalServerException;

public class EmoticonFileUploadException extends InternalServerException {
    public EmoticonFileUploadException() {
        super("이모티콘 업로드에 실패했습니다.");
    }
}
