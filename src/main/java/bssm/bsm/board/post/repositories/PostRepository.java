package bssm.bsm.board.post.repositories;

import bssm.bsm.board.post.entities.Board;
import bssm.bsm.board.post.entities.Post;
import bssm.bsm.board.post.entities.PostCategory;
import bssm.bsm.board.post.entities.PostPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, PostPk> {

    Optional<Post> findByPostPkAndDelete(PostPk postPk, boolean delete);

    // 전체 게시글
    Page<Post> findByPostPkBoardAndDeleteOrderByPostPkIdDesc(Board board, boolean delete, Pageable pageable);
    List<Post> findByPostPkLessThanAndDeleteOrderByPostPkIdDesc(PostPk postPk, boolean delete, Pageable pageable);

    // 카테고리 있는 게시글
    Page<Post> findByCategoryAndDeleteOrderByPostPkIdDesc(PostCategory postCategory, boolean delete, Pageable pageable);
    List<Post> findByPostPkLessThanAndCategoryAndDeleteOrderByPostPkIdDesc(PostPk postPk, PostCategory postCategory, boolean delete, Pageable pageable);

    // 카테고리 없는 게시글
    Page<Post> findByPostPkBoardAndCategoryIdAndDeleteOrderByPostPkIdDesc(Board board, String categoryId, boolean delete, Pageable pageable);
    List<Post> findByPostPkLessThanAndCategoryIdAndDeleteOrderByPostPkIdDesc(PostPk postPk, String categoryId, boolean delete, Pageable pageable);

    // INSERT INTO post (
    //     id,
    //     board_id,
    //     category_id,
    //     usercode,
    //     title,
    //     content,
    //     is_anonymous,
    //     created_at)
    // SELECT
    //     COUNT(id)+1,
    //     :#{#boardId},
    //     :#{#post.categoryId},
    //     :#{#post.usercode},
    //     :#{#post.title},
    //     :#{#post.content},
    //     :#{#post.anonymous},
    //     now()
    // FROM post
    // WHERE board_id = :#{#boardId}
    @Query (value = "INSERT INTO post (id, board_id, category_id, usercode, title, content, is_anonymous, created_at) SELECT COUNT(id)+1, :#{#boardId}, :#{#post.categoryId}, :#{#post.usercode}, :#{#post.title}, :#{#post.content}, :#{#post.anonymous}, now() FROM post WHERE board_id = :#{#boardId}", nativeQuery = true)
    @Modifying
    int insertPost(@Param("post") Post post, @Param("boardId") String boardId);
}