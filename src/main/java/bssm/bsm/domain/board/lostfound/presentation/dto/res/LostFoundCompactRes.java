package bssm.bsm.domain.board.lostfound.presentation.dto.res;

import bssm.bsm.domain.board.lostfound.domain.type.Process;
import bssm.bsm.domain.board.lostfound.domain.type.State;
import lombok.Getter;

@Getter
public class LostFoundCompactRes {

    private final Long id;
    private final String objectName;
    private final String imgSrc;
    private final Process process;
    private final State state;

    public LostFoundCompactRes(Long id, String objectName, String imgSrc, Process process, State state) {
        this.id = id;
        this.objectName = objectName;
        this.imgSrc = imgSrc;
        this.process = process;
        this.state = state;
    }
}
