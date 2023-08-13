package bssm.bsm.domain.board.emoticon.presentation.dto.req;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
public class EmoticonDeleteReq {

    @Positive
    private Long id;

    @NotBlank
    private String msg;

}
