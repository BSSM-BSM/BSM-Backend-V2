package bssm.bsm.domain.board.comment.presentation.dto.req;

import bssm.bsm.domain.board.comment.domain.type.CommentAnonymousType;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private CommentAnonymousType anonymous;
}
