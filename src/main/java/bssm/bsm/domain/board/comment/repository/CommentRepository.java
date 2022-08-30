package bssm.bsm.domain.board.comment.repository;

import bssm.bsm.domain.board.comment.entity.Comment;
import bssm.bsm.domain.board.comment.entity.CommentPk;
import bssm.bsm.domain.board.post.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, CommentPk> {

    List<Comment> findByCommentPkPost(Post post);

    // INSERT INTO comment (
    //     id,
    //     board_id,
    //     post_id,
    //     userCode,
    //     have_child,
    //     depth,
    //     parent_id,
    //     content,
    //     is_anonymous,
    //     created_at)
    // SELECT
    //     COUNT(id)+1,
    //     :#{#boardId},
    //     :#{#postId},
    //     :#{#c.user_code},
    //     :#{#c.haveChild},
    //     :#{#c.depth},
    //     :#{#c.parentId},
    //     :#{#c.content},
    //     :#{#c.anonymous},
    //     now()
    // FROM comment
    // WHERE
    //     board_id = :#{#boardId} AND
    //     post_id = :#{#postId}
    @Query(value = "INSERT INTO comment (id, board_id, post_id, user_code, have_child, depth, parent_id, content, is_anonymous, created_at) SELECT COUNT(id)+1, :#{#boardId}, :#{#postId}, :#{#c.usercode}, :#{#c.haveChild}, :#{#c.depth}, :#{#c.parentId}, :#{#c.content}, :#{#c.anonymous}, now() FROM comment WHERE board_id = :#{#boardId} AND post_id = :#{#postId}", nativeQuery = true)
    @Modifying
    int insertComment(@Param("c") Comment comment, @Param("boardId") String boardId, @Param("postId") int postId);
}
