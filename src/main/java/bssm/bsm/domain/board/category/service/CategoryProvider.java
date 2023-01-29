package bssm.bsm.domain.board.category.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.category.domain.PostCategoryPk;
import bssm.bsm.domain.board.category.domain.repository.PostCategoryRepository;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryProvider {

    private final HashMap<PostCategoryPk, PostCategory> categoryList = new HashMap<>();
    private final PostCategoryRepository postCategoryRepository;

    @PostConstruct
    public void init() {
        List<PostCategory> postCategories = postCategoryRepository.findAll();
        postCategories.forEach(category -> categoryList.put(category.getPk(), category));
    }

    public PostCategory getCategory(String id, Board board) throws NotFoundException {
        if (id.equals("normal")) return null;

        PostCategory category = categoryList.get(new PostCategoryPk(id, board.getId()));
        if (category == null) throw new NotFoundException("카테고리를 찾을 수 없습니다");
        return category;
    }

}
