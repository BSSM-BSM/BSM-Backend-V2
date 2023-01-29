package bssm.bsm.domain.board.post.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostListRes {

    List<PostRes> posts;
    long totalPages;
    int page;
    int limit;
}
