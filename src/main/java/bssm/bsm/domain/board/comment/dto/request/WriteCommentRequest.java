package bssm.bsm.domain.board.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
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
