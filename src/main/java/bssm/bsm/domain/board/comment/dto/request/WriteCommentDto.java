package bssm.bsm.domain.board.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class WriteCommentDto {

    private int depth;
    private int parentId;
    @NotBlank
    @Size(max = 65535)
    private String content;
    private boolean anonymous;
}
