package bssm.bsm.domain.board.board;

import bssm.bsm.domain.board.board.dto.response.BoardResponse;
import bssm.bsm.domain.board.post.dto.response.PostCategoryResponse;
import bssm.bsm.domain.board.post.entities.Board;
import bssm.bsm.domain.board.post.entities.PostCategory;
import bssm.bsm.domain.board.post.repositories.PostCategoryRepository;
import bssm.bsm.domain.board.utils.BoardUtil;
import bssm.bsm.domain.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardUtil boardUtil;
    private final PostCategoryRepository postCategoryRepository;

    public BoardResponse boardInfo(String boardId, User user) {
        Board board = boardUtil.getBoardAndCheckRole(boardId, user.getRole());
        List<PostCategory> postCategoryList = postCategoryRepository.findByPostCategoryPkBoard(board);
        List<PostCategoryResponse> postCategoryDtoList = new ArrayList<>();

        postCategoryList.forEach(category ->
                postCategoryDtoList.add(
                    PostCategoryResponse.builder()
                            .id(category.getPostCategoryPk().getId())
                            .name(category.getName())
                            .build()
                )
        );

        return BoardResponse.builder()
                .boardId(boardId)
                .boardName(board.getName())
                .subBoardId(board.getSubBoardId())
                .subBoardName(board.getSubBoardName())
                .categoryList(postCategoryDtoList)
                .postPermission(board.getWritePostLevel().getValue() <= user.getLevel().getValue())
                .commentPermission(board.getWriteCommentLevel().getValue() <= user.getLevel().getValue())
                .build();
    }
}
