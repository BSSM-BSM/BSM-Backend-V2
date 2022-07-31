package bssm.bsm.board.post.repositories;

import bssm.bsm.board.post.entities.Board;
import bssm.bsm.board.post.entities.Post;
import bssm.bsm.board.post.entities.PostId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, PostId> {

    Optional<Post> findByPostIdAndDelete(PostId postId, boolean delete);

    List<Post> findByPostIdBoard(Board board);

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