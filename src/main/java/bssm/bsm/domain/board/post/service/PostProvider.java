package bssm.bsm.domain.board.post.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.category.domain.PostCategoryPk;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.PostPk;
import bssm.bsm.domain.board.post.presentation.dto.request.GetPostListRequest;
import bssm.bsm.domain.board.post.presentation.dto.request.PostIdRequest;
import bssm.bsm.domain.board.post.facade.PostFacade;
import bssm.bsm.domain.board.post.domain.PostRepository;
import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.category.service.CategoryProvider;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@Validated
@RequiredArgsConstructor
public class PostProvider {

    private final PostFacade postFacade;
    private final BoardProvider boardUtil;
    private final CategoryProvider categoryUtil;
    private final PostRepository postRepository;

    public Post getPost(PostIdRequest postId) {
        Board board = boardUtil.getBoard(postId.getBoard());
        return postRepository.findByPkAndDelete(new PostPk(postId.getPostId(), board), false).orElseThrow(
                () -> new NotFoundException("게시글을 찾을 수 없습니다")
        );
    }

    public Page<Post> getPostListByOffset(PostCategoryPk postCategoryId, GetPostListRequest dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getLimit());
        // 전체 게시글
        if (postCategoryId.getId().equals("all")) {
            return postRepository.findByPkBoardAndDeleteOrderByPkIdDesc(postCategoryId.getBoard(), false, pageable);
        }
        PostCategory postCategory = categoryUtil.getCategory(postCategoryId);
        // 카테고리 없는 게시글
        if (postCategory == null) {
            return postRepository.findByPkBoardAndCategoryIdAndDeleteOrderByPkIdDesc(postCategoryId.getBoard(), null, false, pageable);
        }
        // 카테고리 있는 게시글
        return postRepository.findByCategoryAndDeleteOrderByPkIdDesc(postCategory, false, pageable);
    }

    public List<Post> getPostListByCursor(PostCategoryPk postCategoryId, GetPostListRequest dto) {
        PostPk postId = new PostPk(dto.getStartPostId(), postCategoryId.getBoard());
        Pageable pageable = Pageable.ofSize(dto.getLimit());
        // 전체 게시글
        if (postCategoryId.getId().equals("all")) {
            return postRepository.findByPkLessThanAndDeleteOrderByPkIdDesc(postId, false, pageable);
        }
        PostCategory postCategory = categoryUtil.getCategory(postCategoryId);
        // 카테고리 없는 게시글
        if (postCategory == null) {
            return postRepository.findByPkLessThanAndCategoryIdAndDeleteOrderByPkIdDesc(postId, null, false, pageable);
        }
        // 카테고리 있는 게시글
        return postRepository.findByPkLessThanAndCategoryAndDeleteOrderByPkIdDesc(postId, postCategory, false, pageable);
    }

}
