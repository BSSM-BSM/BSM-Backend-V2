package bssm.bsm.board.utils;

import bssm.bsm.board.post.entities.PostCategory;
import bssm.bsm.board.post.entities.PostCategoryId;
import bssm.bsm.board.post.repositories.PostCategoryRepository;
import bssm.bsm.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCategoryUtil {

    private final BoardUtil boardUtil;
    private final HashMap<PostCategoryId, PostCategory> categoryList = new HashMap<>();
    private final PostCategoryRepository postCategoryRepository;

    @PostConstruct
    public void init() {
        List<PostCategory> postCategories = postCategoryRepository.findAll();
        postCategories.forEach(category -> {
            category.setId(
                    new PostCategoryId(
                            this.boardUtil.getBoard(category.getId().getBoard().getId()),
                            category.getId().getCategoryId()
                    )
            );
            categoryList.put(category.getId(), category);
        });
    }

    public String getCategoryName(PostCategoryId id) {
        return getCategory(id).getName();
    }

    public PostCategory getCategory(PostCategoryId id) throws NotFoundException {
        if (id.getCategoryId().equals("normal")) return null;

        PostCategory category = categoryList.get(id);
        if (category == null) throw new NotFoundException("카테고리를 찾을 수 없습니다");
        return category;
    }
}
