package bssm.bsm.board.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class GetPostListDto {

    private int page;
    private int limit;
    private String categoryId;
    private int startPostId;
}
