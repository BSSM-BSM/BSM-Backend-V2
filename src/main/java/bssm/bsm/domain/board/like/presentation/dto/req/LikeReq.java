package bssm.bsm.domain.board.like.presentation.dto.req;

import bssm.bsm.domain.board.like.domain.enums.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
public class LikeReq {

    @NotBlank
    private String boardId;

    @Positive
    private long postId;

    @NotNull
    Like like;
}
