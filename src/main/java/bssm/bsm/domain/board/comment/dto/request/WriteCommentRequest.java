package bssm.bsm.domain.board.comment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class WriteCommentRequest {

    @Positive
    private int depth;

    @Positive
    private int parentId;

    @NotBlank
    @Size(max = 65535)
    private String content;

    private boolean anonymous;
}
