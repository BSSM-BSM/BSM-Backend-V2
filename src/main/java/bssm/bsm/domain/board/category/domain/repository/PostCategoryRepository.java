package bssm.bsm.domain.board.category.domain.repository;

import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.category.domain.PostCategoryPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, PostCategoryPk> {}