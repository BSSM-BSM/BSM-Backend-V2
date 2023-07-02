package bssm.bsm.domain.board.like.presentation.dto.req;

import bssm.bsm.domain.board.like.domain.enums.Like;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class LikeReq {

    @NotBlank
    private String boardId;

    @Positive
    private long postId;

    @NotNull
    Like like;
}
