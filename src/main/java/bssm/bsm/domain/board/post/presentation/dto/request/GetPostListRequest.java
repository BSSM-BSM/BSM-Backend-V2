package bssm.bsm.domain.board.post.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class GetPostListRequest {

    @Positive
    private int page;

    @Min(10) @Max(100)
    private int limit;

    @NotBlank
    private String categoryId;

    private int startPostId;
}
