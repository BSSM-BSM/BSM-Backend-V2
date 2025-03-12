package bssm.bsm.domain.board.comment.domain.repository;

import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.board.post.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"childComments", "writer"})
    List<Comment> findAllByPostAndParentIdIsNullOrderById(Post post);

    Optional<Comment> findByIdAndPost(long id, Post post);

}
