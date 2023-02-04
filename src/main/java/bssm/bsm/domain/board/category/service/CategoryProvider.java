package bssm.bsm.domain.board.category.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.category.domain.PostCategoryPk;
import bssm.bsm.domain.board.category.domain.repository.PostCategoryRepository;
import bssm.bsm.domain.board.category.exception.NoSuchCategoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryProvider {

    private final HashMap<PostCategoryPk, PostCategory> categoryList = new HashMap<>();
    private final PostCategoryRepository postCategoryRepository;

    @PostConstruct
    public void init() {
        postCategoryRepository.findAll()
                .forEach(category -> categoryList.put(category.getPk(), category));
    }

    public PostCategory findCategory(String id, Board board) {
        if (id.equals("normal")) return null;

        PostCategory category = categoryList.get(new PostCategoryPk(id, board.getId()));
        if (category == null) throw new NoSuchCategoryException();
        return category;
    }

}
