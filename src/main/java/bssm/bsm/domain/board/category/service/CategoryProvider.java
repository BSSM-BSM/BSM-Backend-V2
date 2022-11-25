package bssm.bsm.domain.board.category.service;

import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.category.domain.PostCategoryPk;
import bssm.bsm.domain.board.category.domain.PostCategoryRepository;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryProvider {

    private final BoardProvider boardProvider;
    private final HashMap<PostCategoryPk, PostCategory> categoryList = new HashMap<>();
    private final PostCategoryRepository postCategoryRepository;

    @PostConstruct
    public void init() {
        List<PostCategory> postCategories = postCategoryRepository.findAll();
        postCategories.forEach(category -> {
            category.setPostCategoryPk(
                    new PostCategoryPk(
                            category.getPostCategoryPk().getId(),
                            this.boardProvider.getBoard(category.getPostCategoryPk().getBoard().getId())
                    )
            );
            categoryList.put(category.getPostCategoryPk(), category);
        });
    }

    public PostCategory getCategory(PostCategoryPk id) throws NotFoundException {
        if (id.getId().equals("normal")) return null;

        PostCategory category = categoryList.get(id);
        if (category == null) throw new NotFoundException("카테고리를 찾을 수 없습니다");
        return category;
    }

}
