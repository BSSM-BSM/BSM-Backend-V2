package bssm.bsm.domain.board.board.dto.response;

import bssm.bsm.domain.board.post.dto.response.PostCategoryResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardResponse {

    String boardId;
    String boardName;
    String subBoardId;
    String subBoardName;
    List<PostCategoryResponse> categoryList;
    boolean postPermission;
    boolean commentPermission;
}
