package bssm.bsm.domain.board.post.repositories;

import bssm.bsm.domain.board.post.entities.Board;
import bssm.bsm.domain.board.post.entities.PostCategory;
import bssm.bsm.domain.board.post.entities.PostCategoryPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCategoryRepository extends JpaRepository<PostCategory, PostCategoryPk> {

    List<PostCategory> findByPostCategoryPkBoard(Board board);
}