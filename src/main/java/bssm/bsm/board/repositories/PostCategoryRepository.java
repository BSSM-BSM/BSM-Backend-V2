package bssm.bsm.board.repositories;

import bssm.bsm.board.entities.PostCategory;
import bssm.bsm.board.entities.PostCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, PostCategoryId> {

}