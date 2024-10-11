package bssm.bsm.domain.board.comment.presentation.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class UpdateNoRecordCommentReq {

    @NotBlank
    private String boardId;

    @Positive
    private long postId;

    @Positive
    private int commentId;
}
