package bssm.bsm.domain.board.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class GetPostListDto {

    @Positive
    private int page;
    @Min(10) @Max(100)
    private int limit;
    private String categoryId;
    private int startPostId;
}
