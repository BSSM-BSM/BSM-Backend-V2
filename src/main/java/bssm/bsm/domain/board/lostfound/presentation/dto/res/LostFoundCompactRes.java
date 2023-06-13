package bssm.bsm.domain.board.lostfound.presentation.dto.res;

import bssm.bsm.domain.board.lostfound.domain.type.Process;
import lombok.Getter;

@Getter
public class LostFoundCompactRes {

    private final Long id;
    private final String objectName;
    private final String imgSrc;
    private final Process process;

    public LostFoundCompactRes(Long id, String objectName, String imgSrc, Process process) {
        this.id = id;
        this.objectName = objectName;
        this.imgSrc = imgSrc;
        this.process = process;
    }
}
