package bssm.bsm.domain.board.comment.presentation.dto.req;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
public class WriteCommentReq {

    @NotBlank
    private String boardId;

    @Positive
    private long postId;

    @PositiveOrZero
    private int depth;

    @PositiveOrZero
    private int parentId;

    @NotBlank
    @Size(max = 65535)
    private String content;

    private boolean anonymous;
}
