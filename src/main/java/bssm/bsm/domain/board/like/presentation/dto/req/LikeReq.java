package bssm.bsm.domain.board.like.presentation.dto.req;

import bssm.bsm.domain.board.like.domain.type.LikeType;
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
    LikeType like;
}
