package bssm.bsm.domain.board.board.service;

import bssm.bsm.domain.board.board.presentation.dto.res.BoardRes;
import bssm.bsm.domain.board.post.presentation.dto.res.PostCategoryRes;
import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
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

    public BoardRes findBoardInfo(String boardId, Optional<User> user) {
        Board board = boardUtil.findBoard(boardId);
        board.checkRole(user.map(User::getRole).orElse(null));

        return board.toResponse(user);
    }

}
