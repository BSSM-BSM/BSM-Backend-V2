package bssm.bsm.board.post.dto.response;

import bssm.bsm.board.post.dto.PostDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostListResponseDto {

    List<PostDto> posts;
    long totalPages;
    int page;
    int limit;
}
