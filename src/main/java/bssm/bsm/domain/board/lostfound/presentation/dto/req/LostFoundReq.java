package bssm.bsm.domain.board.lostfound.presentation.dto.req;

import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

}
