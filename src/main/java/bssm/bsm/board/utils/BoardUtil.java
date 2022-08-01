package bssm.bsm.board.utils;

import bssm.bsm.board.post.entities.Board;
import bssm.bsm.board.post.repositories.BoardRepository;
import bssm.bsm.global.exceptions.NotFoundException;
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

    public Board getBoard(String id) throws NotFoundException {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("게시판을 찾을 수 없습니다");
        return board;
    }
}
