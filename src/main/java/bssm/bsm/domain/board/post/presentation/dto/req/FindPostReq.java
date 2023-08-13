package bssm.bsm.domain.board.post.presentation.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class FindPostReq {

    @NotBlank
    private String boardId;

    @Positive
    private long postId;

}
