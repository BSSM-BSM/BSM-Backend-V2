package bssm.bsm.domain.board.post.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.PostPk;
import bssm.bsm.domain.board.post.presentation.dto.req.GetPostListReq;
import bssm.bsm.domain.board.post.presentation.dto.req.PostReq;
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

    private final CategoryProvider categoryProvider;
    private final PostRepository postRepository;

    public Post getPost(Board board, long postId) {
        return postRepository.findByPkAndDelete(PostPk.create(postId, board), false)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다"));
    }

    public Page<Post> getPostListByOffset(Board board, GetPostListReq req) {
        Pageable pageable = PageRequest.of(req.getPage() - 1, req.getLimit());
        // 전체 게시글
        if (req.getCategory().equals("all")) {
            return postRepository.findByBoardAndDeleteOrderByPkIdDesc(board, false, pageable);
        }
        PostCategory postCategory = categoryProvider.getCategory(req.getCategory(), board);
        // 카테고리 없는 게시글
        if (postCategory == null) {
            return postRepository.findByCategoryIsNullAndBoardAndDeleteOrderByPkIdDesc(board, false, pageable);
        }
        // 카테고리 있는 게시글
        return postRepository.findByCategoryAndDeleteOrderByPkIdDesc(postCategory, false, pageable);
    }

    public List<Post> getPostListByCursor(Board board, GetPostListReq req) {
        Pageable pageable = Pageable.ofSize(req.getLimit());
        // 전체 게시글
        if (req.getCategory().equals("all")) {
            return postRepository.findByBoardAndPkIdLessThanAndDeleteOrderByPkIdDesc(board, req.getStartPostId(), false, pageable);
        }
        PostCategory postCategory = categoryProvider.getCategory(req.getCategory(), board);
        // 카테고리 없는 게시글
        if (postCategory == null) {
            return postRepository.findByCategoryIsNullAndBoardAndPkIdLessThanAndDeleteOrderByPkIdDesc(board, req.getStartPostId(), false, pageable);
        }
        // 카테고리 있는 게시글
        return postRepository.findByCategoryAndPkIdLessThanAndDeleteOrderByPkIdDesc(postCategory, req.getStartPostId(), false, pageable);
    }

}
