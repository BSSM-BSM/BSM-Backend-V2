package bssm.bsm.domain.board.post.domain.repository;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Optional<Post> findByIdAndBoard(Long id, Board board);
}