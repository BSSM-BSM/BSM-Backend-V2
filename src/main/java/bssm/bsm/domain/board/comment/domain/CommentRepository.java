package bssm.bsm.domain.board.comment.domain;

import bssm.bsm.domain.board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, CommentPk> {

    List<Comment> findAllByPkPost(Post post);

    @Query(value = "SELECT COUNT(c) FROM Comment c WHERE c.pk.post.pk.board.id = :boardId AND c.pk.post.pk.id = :postId")
    long countByPostPk(@Param("boardId") String boardId, @Param("postId") long postId);
}
