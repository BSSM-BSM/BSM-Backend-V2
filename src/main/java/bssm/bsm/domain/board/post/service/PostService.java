package bssm.bsm.domain.board.post.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.like.service.LikeProvider;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.type.PostAnonymousType;
import bssm.bsm.domain.board.post.exception.DoNotHavePermissionToModifyPostException;
import bssm.bsm.domain.board.post.exception.DoNotHavePermissionToWritePostOnBoardException;
import bssm.bsm.domain.board.post.presentation.dto.req.DeletePostReq;
import bssm.bsm.domain.board.post.presentation.dto.req.FindPostListReq;
import bssm.bsm.domain.board.post.presentation.dto.req.FindPostReq;
import bssm.bsm.domain.board.post.presentation.dto.req.FindRecentPostListReq;
import bssm.bsm.domain.board.post.presentation.dto.req.UpdatePostReq;
import bssm.bsm.domain.board.post.presentation.dto.req.WritePostReq;
import bssm.bsm.domain.board.post.presentation.dto.res.PostListRes;
import bssm.bsm.domain.board.post.presentation.dto.res.DetailPostRes;
import bssm.bsm.domain.board.post.domain.repository.PostRepository;
import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.category.service.CategoryProvider;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.error.exceptions.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

import java.util.List;

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
    private final PostLogService postLogService;

    public PostListRes findPostList(User nullableUser, @Valid FindPostListReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        checkViewPermission(board, nullableUser);

        List<Post> postList = postProvider.findPostListByCursor(board, req.getStartPostId(), req.getLimit(), req.getCategory());
        return PostListRes.create(postList, req.getLimit());
    }

    public PostListRes findRecentPostList(User nullableUser, @Valid FindRecentPostListReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        checkViewPermission(board, nullableUser);

        List<Post> postList = postProvider.findRecentPostList(board, req.getLimit(), req.getCategory());
        return PostListRes.create(postList, req.getLimit());
    }

    @Transactional
    public DetailPostRes findPost(User nullableUser, @Valid FindPostReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        checkViewPermission(board, nullableUser);
        Post post = postProvider.findPost(board, req.getPostId());
        PostLike postLike = likeProvider.findMyPostLike(nullableUser, post);

        post.increaseViewCount();
        return DetailPostRes.create(post, postLike, nullableUser);
    }

    @Transactional
    public long createPost(User user, WritePostReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        checkWritePermission(board, user);

        PostCategory postCategory = categoryProvider.findCategory(req.getCategoryId(), board);
        Post post = postRepository.save(Post.create(board, user, req.getTitle(), req.getContent(), req.getAnonymous(), postCategory));

        if (req.getAnonymous() == PostAnonymousType.NO_RECORD) {
            postLogService.recordTempLog(post, user);
        }

        return post.getId();
    }

    @Transactional
    public void updatePost(User user, UpdatePostReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        Post post = postProvider.findPost(board, req.getPostId());
        checkPostWriter(post, user);
        PostCategory category = categoryProvider.findCategory(req.getCategoryId(), board);
        post.update(req.getTitle(), req.getContent(), category, req.getAnonymous());

        if (req.getAnonymous() == PostAnonymousType.NO_RECORD) {
            postLogService.recordTempLog(post, user);
        }
    }

    @Transactional
    public void deletePost(User user, DeletePostReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        Post post = postProvider.findPost(board, req.getPostId());
        checkPostWriter(post, user);
        post.delete();
    }

    private void checkPostWriter(Post post, User user) {
        post.getBoard().checkAccessibleRole(user);
        if (!post.hasPermission(user)) throw new DoNotHavePermissionToModifyPostException();
    }

    private void checkViewPermission(Board board, User nullableUser) {
        board.checkAccessibleRole(nullableUser);
        if (!board.isPublicPost() && nullableUser == null) throw new UnAuthorizedException();
    }

    private void checkWritePermission(Board board, User user) {
        board.checkAccessibleRole(user);
        if (board.getWritePostLevel().getValue() > user.getLevel().getValue())
            throw new DoNotHavePermissionToWritePostOnBoardException();
    }

}
