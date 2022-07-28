package bssm.bsm.board;

import bssm.bsm.board.entities.PostCategory;
import bssm.bsm.board.entities.PostCategoryId;
import bssm.bsm.board.repositories.PostCategoryRepository;
import bssm.bsm.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCategoryUtil {

    private HashMap<PostCategoryId, PostCategory> categoryList;

    public PostCategoryUtil(PostCategoryRepository postCategoryRepository) {
        List<PostCategory> postCategories = postCategoryRepository.findAll();
        postCategories.forEach(category -> {
            categoryList.put(category.getId(), category);
        });
    }

    public String getCategoryName(PostCategoryId id) {
        return getCategory(id).getName();
    }

    private PostCategory getCategory(PostCategoryId id) throws NotFoundException {
        PostCategory category = categoryList.get(id);
        if (category == null) throw new NotFoundException("Category not found");
        return category;
    }
}
