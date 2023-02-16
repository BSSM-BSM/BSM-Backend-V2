package bssm.bsm.domain.board.post.domain.repository;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.PostPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, PostPk>, PostRepositoryCustom {

    Optional<Post> findByPkAndDelete(PostPk pk, boolean delete);

    @Query(value = "SELECT COUNT(p) FROM Post p WHERE p.board = :board")
    long countByBoard(@Param("board") Board board);
}