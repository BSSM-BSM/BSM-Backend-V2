package bssm.bsm.domain.board.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostListResponse {

    List<PostResponse> posts;
    long totalPages;
    int page;
    int limit;
}
