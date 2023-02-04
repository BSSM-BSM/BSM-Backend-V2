package bssm.bsm.domain.board.file.exception;

import bssm.bsm.global.error.exceptions.InternalServerException;

public class FileUploadException extends InternalServerException {
    public FileUploadException() {
        super("파일 업로드에 실패했습니다.");
    }
}
