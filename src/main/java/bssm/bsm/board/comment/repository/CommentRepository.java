package bssm.bsm.board.comment.repository;

import bssm.bsm.board.comment.entity.Comment;
import bssm.bsm.board.comment.entity.CommentPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, CommentPk> {

    // INSERT INTO comment (
    //     id,
    //     board_id,
    //     post_id,
    //     usercode,
    //     have_child,
    //     depth,
    //     parent_id,
    //     content,
    //     created_at)
    // SELECT
    //     COUNT(id)+1,
    //     :#{#boardId},
    //     :#{#postId},
    //     :#{#c.usercode},
    //     :#{#c.haveChild},
    //     :#{#c.depth},
    //     :#{#c.parentId},
    //     :#{#c.content},
    //     now()
    // FROM comment
    // WHERE
    //     board_id = :#{#boardId} AND
    //     post_id = :#{#postId}
    @Query(value = "INSERT INTO comment (id, board_id, post_id, usercode, have_child, depth, parent_id, content, created_at) SELECT COUNT(id)+1, :#{#boardId}, :#{#postId}, :#{#c.usercode}, :#{#c.haveChild}, :#{#c.depth}, :#{#c.parentId}, :#{#c.content}, now() FROM comment WHERE board_id = :#{#boardId} AND post_id = :#{#postId}", nativeQuery = true)
    @Modifying
    int insertComment(@Param("c") Comment comment, @Param("boardId") String boardId, @Param("postId") int postId);
}
