package bssm.bsm.domain.board.board.domain.repository;

import bssm.bsm.domain.board.board.domain.Board;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository  extends JpaRepository<Board, String> {

    @Override
    @NotNull
    @EntityGraph(attributePaths = "categories")
    List<Board> findAll();
}