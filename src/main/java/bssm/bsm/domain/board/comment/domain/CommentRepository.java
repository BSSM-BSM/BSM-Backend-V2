package bssm.bsm.domain.board.comment.domain;

import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.PostPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, CommentPk> {

    List<Comment> findAllByPkPost(Post post);

    @Query(value = "SELECT COUNT(c) FROM Comment c WHERE c.pk.post.pk = :postPk")
    long countByPostPk(@Param("postPk") PostPk postPk);
}
