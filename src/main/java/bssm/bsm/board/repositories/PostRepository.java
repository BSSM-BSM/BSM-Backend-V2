package bssm.bsm.board.repositories;

import bssm.bsm.board.entities.Post;
import bssm.bsm.board.entities.PostId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, PostId> {

    // INSERT INTO post (
    //     id,
    //     board_id,
    //     category_id,
    //     usercode,
    //     title,
    //     content,
    //     created_at)
    // SELECT
    //     COUNT(id)+1,
    //     :#{#boardId},
    //     :#{#categoryId},
    //     :#{#post.usercode},
    //     :#{#post.title},
    //     :#{#post.content},
    //     now()
    // FROM post
    // WHERE board_id = :#{#boardId}
    @Query (value = "INSERT INTO post (id, board_id, category_id, usercode, title, content, created_at) SELECT COUNT(id)+1, :#{#boardId}, :#{#categoryId}, :#{#post.usercode}, :#{#post.title}, :#{#post.content}, now() FROM post WHERE board_id = :#{#boardId}", nativeQuery = true)
    @Modifying
    int insertPost(@Param("post") Post post, @Param("boardId") String boardId, @Param("categoryId") String categoryId);
}