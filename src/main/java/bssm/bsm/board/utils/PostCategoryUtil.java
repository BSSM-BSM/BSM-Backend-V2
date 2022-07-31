package bssm.bsm.board.utils;

import bssm.bsm.board.post.entities.PostCategory;
import bssm.bsm.board.post.entities.PostCategoryId;
import bssm.bsm.board.post.repositories.PostCategoryRepository;
import bssm.bsm.global.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PostCategoryUtil {

    private final HashMap<PostCategoryId, PostCategory> categoryList = new HashMap<>();

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
