package bssm.bsm.domain.board.post.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.like.service.LikeProvider;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.PostPk;
import bssm.bsm.domain.board.post.exception.DoNotHavePermissionToModifyPostException;
import bssm.bsm.domain.board.post.exception.DoNotHavePermissionToWritePostOnBoardException;
import bssm.bsm.domain.board.post.presentation.dto.req.UpdatePostReq;
import bssm.bsm.domain.board.post.presentation.dto.res.PostRes;
import bssm.bsm.domain.board.post.presentation.dto.req.WritePostReq;
import bssm.bsm.domain.board.post.presentation.dto.res.PostListRes;
import bssm.bsm.domain.board.post.presentation.dto.res.DetailPostRes;
import bssm.bsm.domain.board.post.domain.repository.PostRepository;
import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.category.service.CategoryProvider;
import bssm.bsm.domain.board.post.presentation.dto.req.GetPostListReq;
import bssm.bsm.domain.board.post.presentation.dto.req.PostReq;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.error.exceptions.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
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

    private final PostRepository postRepository;

    private final BoardProvider boardProvider;
    private final CategoryProvider categoryProvider;
    private final PostProvider postProvider;
    private final LikeProvider likeProvider;

    public PostListRes findPostList(Optional<User> user, @Valid GetPostListReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        board.checkPermissionByUserRole(user.map(User::getRole).orElse(null));
        checkViewPermission(board, user);

        List<Post> postList = postProvider.findPostListByCursor(board, req);
        List<PostRes> postResList = postList.stream()
                .map(PostRes::create)
                .toList();
        return PostListRes.create(postResList, req.getLimit());
    }

    public DetailPostRes findPost(Optional<User> user, @Valid PostReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        checkViewPermission(board, user);
        Post post = postProvider.findPost(board, req.getPostId());
        PostLike postLike = likeProvider.findMyPostLike(user, post);

        post.increaseViewCnt();
        return DetailPostRes.create(post, postLike, user);
    }

    @Transactional
    public long createPost(User user, WritePostReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        board.checkPermissionByUserRole(user.getRole());
        checkWritePermission(board, user);

        PostCategory postCategory = categoryProvider.findCategory(req.getCategoryId(), board);
        PostPk postPk = PostPk.create(postProvider.getNewPostId(board), board);
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
        Board board = boardProvider.findBoard(req.getBoardId());
        Post post = postProvider.findPost(board, req.getPostId());
        checkPostWriter(post, user);
        PostCategory category = categoryProvider.findCategory(req.getCategoryId(), board);

        post.update(post.getTitle(), post.getContent(), category, req.isAnonymous());
    }

    @Transactional
    public void deletePost(User user, PostReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        Post post = postProvider.findPost(board, req.getPostId());
        checkPostWriter(post, user);

        post.delete();
    }

    private void checkPostWriter(Post post, User user) {
        if (!post.checkPermission(user)) throw new DoNotHavePermissionToModifyPostException();
    }

    public void checkViewPermission(Board board, Optional<User> user) {
        if (!board.isPublicPost() && user.isEmpty()) throw new UnAuthorizedException();
    }

    public void checkWritePermission(Board board, User user) {
        if (board.getWritePostLevel().getValue() > user.getLevel().getValue()) throw new DoNotHavePermissionToWritePostOnBoardException();
    }


}
