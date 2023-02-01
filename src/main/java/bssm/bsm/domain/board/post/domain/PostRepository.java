package bssm.bsm.domain.board.post.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, PostPk> {

    Optional<Post> findByPkAndDelete(PostPk pk, boolean delete);

    // 전체 게시글
    Page<Post> findByBoardAndDeleteOrderByPkIdDesc(Board board, boolean delete, Pageable pageable);
    List<Post> findByBoardAndPkIdLessThanAndDeleteOrderByPkIdDesc(Board board, long id, boolean delete, Pageable pageable);

    // 카테고리 있는 게시글
    Page<Post> findByCategoryAndDeleteOrderByPkIdDesc(PostCategory postCategory, boolean delete, Pageable pageable);
    List<Post> findByCategoryAndPkIdLessThanAndDeleteOrderByPkIdDesc(PostCategory category, long id, boolean delete, Pageable pageable);

    // 카테고리 없는 게시글
    Page<Post> findByCategoryIsNullAndBoardAndDeleteOrderByPkIdDesc(Board board, boolean delete, Pageable pageable);
    List<Post> findByCategoryIsNullAndBoardAndPkIdLessThanAndDeleteOrderByPkIdDesc(Board board, long id, boolean delete, Pageable pageable);

    @Query(value = "SELECT COUNT(p) FROM Post p WHERE p.board = :board")
    long countByBoard(@Param("board") Board board);
}