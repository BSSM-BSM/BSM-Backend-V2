package bssm.bsm.board.repositories;

import bssm.bsm.board.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository  extends JpaRepository<Board, String> {

}