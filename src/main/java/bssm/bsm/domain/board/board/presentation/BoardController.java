package bssm.bsm.domain.board.board.presentation;

import bssm.bsm.domain.board.board.presentation.dto.res.BoardRes;
import bssm.bsm.domain.board.board.service.BoardService;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CurrentUser userUtil;

    @GetMapping("/{boardId}")
    public BoardRes boardInfo(@PathVariable String boardId) {
        return boardService.boardInfo(boardId, userUtil.getOptionalUser());
    }

}
