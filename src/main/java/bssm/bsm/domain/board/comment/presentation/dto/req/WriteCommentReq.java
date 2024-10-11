package bssm.bsm.domain.board.comment.presentation.dto.req;

import bssm.bsm.domain.board.comment.domain.type.CommentAnonymousType;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

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
