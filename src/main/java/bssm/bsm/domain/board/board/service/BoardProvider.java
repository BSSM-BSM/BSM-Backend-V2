package bssm.bsm.domain.board.board.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.board.domain.repository.BoardRepository;
import bssm.bsm.domain.board.board.exception.NoSuchBoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class BoardProvider {

    private final HashMap<String, Board> boardList = new HashMap<>();
    private final BoardRepository boardRepository;

    @PostConstruct
    public void init() {
        boardRepository.findAll()
                .forEach(board -> boardList.put(board.getId(), board));
    }

    public Board findBoard(String id) {
        Board board = boardList.get(id);
        if (board == null) throw new NoSuchBoardException();
        return board;
    }

}
