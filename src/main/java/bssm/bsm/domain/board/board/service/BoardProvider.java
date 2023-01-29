package bssm.bsm.domain.board.board.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.board.domain.BoardRepository;
import bssm.bsm.global.error.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BoardProvider {

    private final HashMap<String, Board> boardList = new HashMap<>();

    public BoardProvider(BoardRepository boardRepository) {
        boardRepository.findAll()
                .forEach(board -> boardList.put(board.getId(), board));
    }

    public Board getBoard(String id) throws NotFoundException {
        Board board = boardList.get(id);
        if (board == null) throw new NotFoundException("게시판을 찾을 수 없습니다");
        return board;
    }

}
