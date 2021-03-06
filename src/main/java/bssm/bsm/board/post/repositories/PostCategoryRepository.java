package bssm.bsm.board.post.repositories;

import bssm.bsm.board.post.entities.PostCategory;
import bssm.bsm.board.post.entities.PostCategoryPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, PostCategoryPk> {

}