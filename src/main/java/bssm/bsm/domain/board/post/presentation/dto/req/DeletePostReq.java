package bssm.bsm.domain.board.post.presentation.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class DeletePostReq {

    @NotBlank
    private String boardId;

    @Positive
    private long postId;

}
