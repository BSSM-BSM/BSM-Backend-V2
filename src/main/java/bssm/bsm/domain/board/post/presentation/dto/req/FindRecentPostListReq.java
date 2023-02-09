package bssm.bsm.domain.board.post.presentation.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class FindRecentPostListReq {

    @NotBlank
    private String boardId;

    @Min(10) @Max(100)
    private int limit;

    @NotBlank
    private String category;

}
