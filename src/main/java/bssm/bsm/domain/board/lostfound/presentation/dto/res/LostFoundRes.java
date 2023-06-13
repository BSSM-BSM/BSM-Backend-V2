package bssm.bsm.domain.board.lostfound.presentation.dto.res;

import bssm.bsm.domain.board.lostfound.domain.LostFound;
import bssm.bsm.domain.board.lostfound.domain.type.Process;
import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;


public class LostFoundRes {
    @JsonProperty
    private final Long id;

    @JsonProperty
    private final String objectName;

    @JsonProperty
    private final String imgSrc;

    @JsonProperty
    private final String location;

    @JsonProperty
    private final LocalDateTime findDateTime;

    @JsonProperty
    private final String description;

    @JsonProperty
    private final Process process;

    @JsonProperty
    private final UserRes foundUser;

    public LostFoundRes(LostFound newLostFound) {
        this.id = newLostFound.getId();
        this.objectName = newLostFound.getObjectName();
        this.imgSrc = newLostFound.getImgSrc();
        this.findDateTime = newLostFound.getFindDateTime();
        this.description = newLostFound.getDescription();
        this.process = newLostFound.getProcess();
        this.location = newLostFound.getLocation();
        this.foundUser = new UserRes(newLostFound.getFoundUser());
    }

}
