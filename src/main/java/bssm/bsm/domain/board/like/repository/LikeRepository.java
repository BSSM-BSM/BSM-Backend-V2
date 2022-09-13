package bssm.bsm.domain.board.like.repository;

import bssm.bsm.domain.board.like.entity.PostLike;
import bssm.bsm.domain.board.like.entity.PostLikePk;
import bssm.bsm.domain.board.post.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, PostLikePk> {

    Optional<PostLike> findByPostLikePkPost(Post post);

    // INSERT INTO post_like (
    //     id,
    //     board_id,
    //     post_id,
    //     user_code,
    //     is_like)
    // SELECT
    //     COUNT(id)+1,
    //     :#{#boardId},
    //     :#{#postId},
    //     :#{#l.userCode},
    //     :#{#l.like}
    // FROM post_like
    // WHERE
    //     board_id = :#{#boardId} AND
    //     post_id = :#{#postId}
    @Query(value = "INSERT INTO post_like (id, board_id, post_id, user_code, is_like) SELECT COUNT(id)+1, :#{#boardId}, :#{#postId}, :#{#l.userCode}, :#{#l.like} FROM post_like WHERE board_id = :#{#boardId} AND post_id = :#{#postId}", nativeQuery = true)
    @Modifying
    void insertLike(@Param("l") PostLike postLike, @Param("boardId") String boardId, @Param("postId") int postId);
}
