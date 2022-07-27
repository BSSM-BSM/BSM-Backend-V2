package bssm.bsm.board;

import bssm.bsm.board.entities.Board;
import bssm.bsm.board.repositories.BoardRepository;
import bssm.bsm.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardUtil {

    private HashMap<String, Board> boardList;

    public BoardUtil(BoardRepository boardRepository) {
        List<Board> boards = boardRepository.findAll();
        boards.forEach(board -> {
            boardList.put(board.getId(), board);
        });
    }

    public String getBoardName(String id) {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("Board not found");
        return board.getName();
    }

    public String getSubBoardName(String id) {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("Board not found");
        return board.getSubBoardName();
    }

    public String getSubBoardId(String id) {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("Board not found");
        return board.getSubBoardId();
    }

    public boolean isPublicPost(String id) {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("Board not found");
        return board.isPublicPost();
    }

    public int getWritePostLevel(String id) {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("Board not found");
        return board.getWritePostLevel();
    }

    public boolean isPublicComment(String id) {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("Board not found");
        return board.isPublicComment();
    }

    public int getWriteCommentLevel(String id) {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("Board not found");
        return board.getWriteCommentLevel();
    }
}
