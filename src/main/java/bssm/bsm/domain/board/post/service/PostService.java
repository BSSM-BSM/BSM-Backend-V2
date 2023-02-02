package bssm.bsm.domain.board.post.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.like.service.LikeProvider;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.PostPk;
import bssm.bsm.domain.board.post.presentation.dto.req.UpdatePostReq;
import bssm.bsm.domain.board.post.presentation.dto.res.PostRes;
import bssm.bsm.domain.board.post.presentation.dto.req.WritePostReq;
import bssm.bsm.domain.board.post.presentation.dto.res.PostListRes;
import bssm.bsm.domain.board.post.presentation.dto.res.DetailPostRes;
import bssm.bsm.domain.board.post.facade.PostFacade;
import bssm.bsm.domain.board.post.domain.PostRepository;
import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.category.service.CategoryProvider;
import bssm.bsm.domain.board.post.presentation.dto.req.GetPostListReq;
import bssm.bsm.domain.board.post.presentation.dto.req.PostReq;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostFacade postFacade;
    private final PostRepository postRepository;
    private final BoardProvider boardProvider;
    private final CategoryProvider categoryProvider;
    private final PostProvider postProvider;
    private final LikeProvider likeProvider;

    public PostListRes postList(Optional<User> user, @Valid GetPostListReq req) {
        Board board = boardProvider.getBoard(req.getBoardId());
        board.checkRole(user.map(User::getRole).orElse(null));
        postFacade.checkViewPermission(board, user);

        final boolean pageMode = req.getStartPostId() < 0;
        List<Post> posts;
        Page<Post> pages = null;
        if (pageMode) {
             pages = postProvider.getPostListByOffset(board, req);
             posts = pages.getContent();
        } else {
            posts = postProvider.getPostListByCursor(board, req);
        }

        List<PostRes> postResList = posts.stream()
                .map(PostRes::create)
                .toList();

        if (pageMode) {
            return PostListRes.builder()
                    .posts(postResList)
                    .totalPages(pages.getTotalPages())
                    .page(req.getPage())
                    .limit(req.getLimit())
                    .build();
        } else {
            return PostListRes.builder()
                    .posts(postResList)
                    .limit(req.getLimit())
                    .build();
        }
    }

    public DetailPostRes viewPost(Optional<User> user, @Valid PostReq req) {
        Board board = boardProvider.getBoard(req.getBoardId());
        postFacade.checkViewPermission(board, user);
        Post post = postProvider.getPost(board, req.getPostId());
        PostLike postLike = likeProvider.getMyPostLike(user, post);

        post.increaseViewCnt();
        return DetailPostRes.create(post, postLike, user);
    }

    @Transactional
    public long createPost(User user, WritePostReq req) {
        Board board = boardProvider.getBoard(req.getBoardId());
        board.checkRole(user.getRole());
        postFacade.checkWritePermission(board, user);

        PostCategory postCategory = categoryProvider.getCategory(req.getCategoryId(), board);
        PostPk postPk = PostPk.create(postRepository.countByBoard(board) + 1, board);
        Post newPost = Post.builder()
                .pk(postPk)
                .writer(user)
                .category(postCategory)
                .title(req.getTitle())
                .content(req.getContent())
                .createdAt(new Date())
                .anonymous(req.isAnonymous())
                .build();
        postRepository.save(newPost);
        return newPost.getPk().getId();
    }

    @Transactional
    public void updatePost(User user, UpdatePostReq req) {
        Board board = boardProvider.getBoard(req.getBoardId());
        Post post = postProvider.getPost(board, req.getPostId());
        checkPermission(post, user);
        PostCategory category = categoryProvider.getCategory(req.getCategoryId(), board);

        post.update(post.getTitle(), post.getContent(), category, req.isAnonymous());
    }

    @Transactional
    public void deletePost(User user, PostReq req) {
        Board board = boardProvider.getBoard(req.getBoardId());
        Post post = postProvider.getPost(board, req.getPostId());
        checkPermission(post, user);

        post.delete();
    }

    private void checkPermission(Post post, User user) {
        if (!post.checkPermission(user)) throw new ForbiddenException("권한이 없습니다");
    }

}
