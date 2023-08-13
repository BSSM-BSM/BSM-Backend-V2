package bssm.bsm.domain.board.board.presentation.dto.res;

import bssm.bsm.domain.board.post.presentation.dto.res.PostCategoryRes;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardRes {

    String boardId;
    String boardName;
    String subBoardId;
    String subBoardName;
    List<PostCategoryRes> categoryList;
    boolean postPermission;
    boolean commentPermission;
}
