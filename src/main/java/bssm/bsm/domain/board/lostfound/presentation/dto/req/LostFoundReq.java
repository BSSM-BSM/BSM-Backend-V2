package bssm.bsm.domain.board.lostfound.presentation.dto.req;

import bssm.bsm.domain.board.lostfound.domain.type.State;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class LostFoundReq {

    @NotBlank
    private String objectName;

    @NotBlank
    private String imgSrc;

    @NotBlank
    private String location;

    @NotNull
    private LocalDateTime findDateTime;

    @NotBlank
    private String description;

    @NotNull
    private State state;

}
