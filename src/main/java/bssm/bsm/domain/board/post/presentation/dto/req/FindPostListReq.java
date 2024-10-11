package bssm.bsm.domain.board.post.presentation.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

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
