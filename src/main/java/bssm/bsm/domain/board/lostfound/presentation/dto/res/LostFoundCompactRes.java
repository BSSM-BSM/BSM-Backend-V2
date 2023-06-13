package bssm.bsm.domain.board.lostfound.presentation.dto.res;

import bssm.bsm.domain.board.lostfound.domain.type.Process;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LostFoundCompactRes {
    @JsonProperty
    private final Long id;

    @JsonProperty
    private final String objectName;

    @JsonProperty
    private final String imgSrc;

    @JsonProperty
    private final Process process;

    public LostFoundCompactRes(Long id, String objectName, String imgSrc, Process process) {
        this.id = id;
        this.objectName = objectName;
        this.imgSrc = imgSrc;
        this.process = process;
    }
}
