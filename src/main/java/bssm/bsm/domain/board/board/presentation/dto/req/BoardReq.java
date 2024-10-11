package bssm.bsm.domain.board.board.presentation.dto.req;

import lombok.Getter;

import jakarta.validation.constraints.NotBlank;

@Getter
public class BoardReq {

    @NotBlank
    private String boardId;
}
