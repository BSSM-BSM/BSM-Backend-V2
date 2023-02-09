package bssm.bsm.domain.board.post.presentation.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class FindPostListReq {

    @NotBlank
    private String boardId;

    @Min(10) @Max(100)
    private int limit;

    @NotBlank
    private String category;

    @Positive
    private long startPostId;
}
