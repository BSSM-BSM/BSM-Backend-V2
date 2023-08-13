package bssm.bsm.domain.board.board.service;

import bssm.bsm.domain.board.board.presentation.dto.res.BoardRes;
import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardProvider boardUtil;

    public BoardRes findBoardInfo(String boardId, User nullableUser) {
        Board board = boardUtil.findBoard(boardId);
        board.checkAccessibleRole(nullableUser);

        return board.toResponse(nullableUser);
    }

}
