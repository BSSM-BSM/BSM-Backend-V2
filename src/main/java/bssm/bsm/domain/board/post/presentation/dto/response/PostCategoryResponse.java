package bssm.bsm.domain.board.post.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCategoryResponse {

    private String id;
    private String name;
}
