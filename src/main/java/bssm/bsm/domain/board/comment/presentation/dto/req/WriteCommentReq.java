package bssm.bsm.domain.board.comment.presentation.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class WriteCommentReq {

    @PositiveOrZero
    private int depth;

    @PositiveOrZero
    private int parentId;

    @NotBlank
    @Size(max = 65535)
    private String content;

    private boolean anonymous;
}