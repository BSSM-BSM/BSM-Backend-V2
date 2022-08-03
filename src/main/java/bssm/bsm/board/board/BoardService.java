package bssm.bsm.board.board;

import bssm.bsm.board.board.dto.response.BoardResponseDto;
import bssm.bsm.board.post.dto.PostCategoryDto;
import bssm.bsm.board.post.entities.Board;
import bssm.bsm.board.post.entities.PostCategory;
import bssm.bsm.board.post.repositories.PostCategoryRepository;
import bssm.bsm.board.utils.BoardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardUtil boardUtil;
    private final PostCategoryRepository postCategoryRepository;

    public BoardResponseDto boardInfo(String boardId) {
        Board board = boardUtil.getBoard(boardId);
        List<PostCategory> postCategoryList = postCategoryRepository.findByPostCategoryPkBoard(board);
        List<PostCategoryDto> postCategoryDtoList = new ArrayList<>();

        postCategoryList.forEach(category ->
                postCategoryDtoList.add(
                    PostCategoryDto.builder()
                            .id(category.getPostCategoryPk().getId())
                            .name(category.getName())
                            .build()
                )
        );

        return BoardResponseDto.builder()
                .boardId(boardId)
                .boardName(board.getName())
                .subBoardId(board.getSubBoardId())
                .subBoardName(board.getSubBoardName())
                .categoryList(postCategoryDtoList)
                .build();
    }
}
