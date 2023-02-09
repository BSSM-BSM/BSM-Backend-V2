package bssm.bsm.domain.board.post.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.PostPk;
import bssm.bsm.domain.board.post.exception.NoSuchPostException;
import bssm.bsm.domain.board.post.presentation.dto.req.FindPostListReq;
import bssm.bsm.domain.board.post.domain.repository.PostRepository;
import bssm.bsm.domain.board.category.service.CategoryProvider;
import bssm.bsm.domain.board.post.presentation.dto.req.FindRecentPostListReq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostProvider {

    private final CategoryProvider categoryProvider;
    private final PostRepository postRepository;

    public Post findPost(Board board, long postId) {
        return postRepository.findByPkAndDelete(PostPk.create(postId, board), false)
                .orElseThrow(NoSuchPostException::new);
    }

    public Long getNewPostId(Board board) {
        return postRepository.countByBoard(board) + 1;
    }

    public List<Post> findRecentPostList(Board board, FindRecentPostListReq req) {
        Pageable pageable = Pageable.ofSize(req.getLimit());
        // 전체 게시글
        if (req.getCategory().equals("all")) {
            return postRepository.findByBoardAndDeleteOrderByPkIdDesc(board, false, pageable);
        }
        PostCategory postCategory = categoryProvider.findCategory(req.getCategory(), board);
        // 카테고리 없는 게시글
        if (postCategory == null) {
            return postRepository.findByCategoryIsNullAndBoardAndDeleteOrderByPkIdDesc(board, false, pageable);
        }
        // 카테고리 있는 게시글
        return postRepository.findByCategoryAndDeleteOrderByPkIdDesc(postCategory, false, pageable);
    }

    public List<Post> findPostListByCursor(Board board, FindPostListReq req) {
        Pageable pageable = Pageable.ofSize(req.getLimit());
        // 전체 게시글
        if (req.getCategory().equals("all")) {
            return postRepository.findByBoardAndPkIdLessThanAndDeleteOrderByPkIdDesc(board, req.getStartPostId(), false, pageable);
        }
        PostCategory postCategory = categoryProvider.findCategory(req.getCategory(), board);
        // 카테고리 없는 게시글
        if (postCategory == null) {
            return postRepository.findByCategoryIsNullAndBoardAndPkIdLessThanAndDeleteOrderByPkIdDesc(board, req.getStartPostId(), false, pageable);
        }
        // 카테고리 있는 게시글
        return postRepository.findByCategoryAndPkIdLessThanAndDeleteOrderByPkIdDesc(postCategory, req.getStartPostId(), false, pageable);
    }

}
