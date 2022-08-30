package bssm.bsm.domain.board.board.dto.response;

import bssm.bsm.domain.board.post.dto.PostCategoryDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardResponseDto {

    String boardId;
    String boardName;
    String subBoardId;
    String subBoardName;
    List<PostCategoryDto> categoryList;
}
