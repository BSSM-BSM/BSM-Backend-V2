package bssm.bsm.domain.board.emoticon.presentation.dto.req;

import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Getter
public class EmoticonDeleteReq {

    @Positive
    private Long id;

    @NotBlank
    private String msg;

}
