package bssm.bsm.domain.board.board.service;

import bssm.bsm.domain.board.board.presentation.dto.response.BoardResponse;
import bssm.bsm.domain.board.post.presentation.dto.response.PostCategoryResponse;
import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.category.domain.PostCategoryRepository;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardProvider boardUtil;
    private final PostCategoryRepository postCategoryRepository;

    public BoardResponse boardInfo(String boardId, Optional<User> user) {
        Board board = boardUtil.getBoard(boardId);
        board.checkRole(user.map(User::getRole).orElse(null));

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

        return board.toResponse(user, postCategoryDtoList);
    }

}
