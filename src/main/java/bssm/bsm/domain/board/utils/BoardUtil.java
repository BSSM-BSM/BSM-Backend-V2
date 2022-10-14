package bssm.bsm.domain.board.utils;

import bssm.bsm.domain.board.post.entities.Board;
import bssm.bsm.domain.board.post.repositories.BoardRepository;
import bssm.bsm.domain.user.type.UserLevel;
import bssm.bsm.domain.user.type.UserRole;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import bssm.bsm.global.error.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BoardUtil {

    private final HashMap<String, Board> boardList = new HashMap<>();

    public BoardUtil(BoardRepository boardRepository) {
        List<Board> boards = boardRepository.findAll();
        boards.forEach(board -> {
            boardList.put(board.getId(), board);
        });
    }

    public Board getBoard(String id) throws NotFoundException {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("게시판을 찾을 수 없습니다");
        return board;
    }

    public Board getBoardAndCheckRole(String id, UserRole role) throws NotFoundException {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("게시판을 찾을 수 없습니다");
        if (board.getAccessibleRole() != null && board.getAccessibleRole() != role) {
            switch (board.getAccessibleRole()) {
                case STUDENT -> throw new ForbiddenException("학생만 접근할 수 있는 게시판입니다");
                case TEACHER -> throw new ForbiddenException("선생님만 접근할 수 있는 게시판입니다");
            }
        }
        return board;
    }
}
