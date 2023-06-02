package bssm.bsm.domain.board.lostfound.presentation.dto.req;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
public class LostFoundReq {

    @NotBlank
    private String objectName;

    private String imgSrc;

    private String location;

    private LocalDateTime findDateTime;

    @NotBlank
    private String description;

}
