package bssm.bsm.board.utils;

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
        return getBoard(id).getName();
    }

    public String getSubBoardName(String id) {
        return getBoard(id).getSubBoardName();
    }

    public String getSubBoardId(String id) {
        return getBoard(id).getSubBoardId();
    }

    public boolean isPublicPost(String id) {
        return getBoard(id).isPublicPost();
    }

    public int getWritePostLevel(String id) {
        return getBoard(id).getWritePostLevel();
    }

    public boolean isPublicComment(String id) {
        return getBoard(id).isPublicComment();
    }

    public int getWriteCommentLevel(String id) {
        return getBoard(id).getWriteCommentLevel();
    }

    private Board getBoard(String id) throws NotFoundException {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("Board not found");
        return board;
    }
}
