package bssm.bsm.board.post.repositories;

import bssm.bsm.board.post.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository  extends JpaRepository<Board, String> {

}