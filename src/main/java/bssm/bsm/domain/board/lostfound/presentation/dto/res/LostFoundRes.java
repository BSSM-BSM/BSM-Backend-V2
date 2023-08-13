package bssm.bsm.domain.board.lostfound.presentation.dto.res;

import bssm.bsm.domain.board.lostfound.domain.LostFound;
import bssm.bsm.domain.board.lostfound.domain.type.Process;
import bssm.bsm.domain.board.lostfound.domain.type.State;
import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LostFoundRes {

    private final Long id;
    private final String objectName;
    private final String imgSrc;
    private final String location;
    private final LocalDateTime findDateTime;
    private final String description;
    private final Process process;
    private final State state;

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
        this.state = newLostFound.getState();
        this.foundUser = new UserRes(newLostFound.getFoundUser());
    }

}
