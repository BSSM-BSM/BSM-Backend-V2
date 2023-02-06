package bssm.bsm.domain.board.emoticon.exception;

import bssm.bsm.global.error.exceptions.BadRequestException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotAllowedEmoticonFileExtensionException extends BadRequestException {

    private static Map<String, String> toMsgField(List<Integer> fileIdxList) {
        Map<String, String> msgMap = new HashMap<>();
        fileIdxList.forEach(idx ->
                msgMap.put("item " + idx, (idx + 1) + "번 이모티콘에 허용되지 않는 파일 확장자가 존재합니다."));
        return msgMap;
    }

    public NotAllowedEmoticonFileExtensionException(List<Integer> fileIdxList) {
        super(toMsgField(fileIdxList));
    }
}
