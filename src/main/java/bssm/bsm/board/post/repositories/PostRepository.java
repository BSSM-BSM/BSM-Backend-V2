package bssm.bsm.board.post.repositories;

import bssm.bsm.board.post.entities.Board;
import bssm.bsm.board.post.entities.Post;
import bssm.bsm.board.post.entities.PostCategory;
import bssm.bsm.board.post.entities.PostId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, PostId> {

    Optional<Post> findByPostIdAndDelete(PostId postId, boolean delete);

    // 전체 게시글
    Page<Post> findByPostIdBoardAndDeleteOrderByPostIdDesc(Board board, boolean delete, Pageable pageable);
    List<Post> findByPostIdLessThanAndDeleteOrderByPostIdDesc(PostId postId, boolean delete, Pageable pageable);

    // 카테고리 있는 게시글
    Page<Post> findByCategoryAndDeleteOrderByPostIdDesc(PostCategory postCategory, boolean delete, Pageable pageable);
    List<Post> findByPostIdLessThanAndCategoryAndDeleteOrderByPostIdDesc(PostId postId, PostCategory postCategory, boolean delete, Pageable pageable);

    // 카테고리 없는 게시글
    Page<Post> findByPostIdBoardAndCategoryIdAndDeleteOrderByPostIdDesc(Board board, String categoryId, boolean delete, Pageable pageable);
    List<Post> findByPostIdLessThanAndCategoryIdAndDeleteOrderByPostIdDesc(PostId postId, String categoryId, boolean delete, Pageable pageable);

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
    //     :#{#post.categoryId},
    //     :#{#post.usercode},
    //     :#{#post.title},
    //     :#{#post.content},
    //     now()
    // FROM post
    // WHERE board_id = :#{#boardId}
    @Query (value = "INSERT INTO post (id, board_id, category_id, usercode, title, content, created_at) SELECT COUNT(id)+1, :#{#boardId}, :#{#categoryId}, :#{#post.usercode}, :#{#post.title}, :#{#post.content}, now() FROM post WHERE board_id = :#{#boardId}", nativeQuery = true)
    @Modifying
    int insertPost(@Param("post") Post post, @Param("boardId") String boardId);
}