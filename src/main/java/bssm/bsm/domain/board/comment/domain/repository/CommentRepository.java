package bssm.bsm.domain.board.comment.domain.repository;

import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.board.comment.domain.CommentPk;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.PostPk;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, CommentPk> {

    @EntityGraph(attributePaths = {"childComments", "writer"})
    List<Comment> findAllByPostAndParentIdIsNullOrderByPkId(Post post);

    Optional<Comment> findByPkIdAndPost(long id, Post post);

    @Query(value = "SELECT COUNT(c) FROM Comment c WHERE c.post = :post")
    long countByPost(@Param("post") Post post);
}
