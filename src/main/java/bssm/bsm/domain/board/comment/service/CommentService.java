package bssm.bsm.domain.board.comment.service;

import bssm.bsm.domain.board.anonymous.service.AnonymousUserIdProvider;
import bssm.bsm.domain.board.comment.exception.DoNotHavePermissionToDeleteCommentException;
import bssm.bsm.domain.board.comment.exception.DoNotHavePermissionToWriteCommentOnBoardException;
import bssm.bsm.domain.board.comment.exception.NoSuchCommentException;
import bssm.bsm.domain.board.comment.presentation.dto.req.DeleteCommentReq;
import bssm.bsm.domain.board.comment.presentation.dto.req.FindCommentTreeReq;
import bssm.bsm.domain.board.comment.presentation.dto.req.WriteCommentReq;
import bssm.bsm.domain.board.comment.presentation.dto.res.CommentRes;
import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.board.comment.domain.repository.CommentRepository;
import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.post.service.PostProvider;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.error.exceptions.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentProvider commentProvider;
    private final BoardProvider boardProvider;
    private final PostProvider postProvider;
    private final AnonymousUserIdProvider anonymousUserIdProvider;

    private final CommentRepository commentRepository;

    @Transactional
    public void writeComment(User user, @Valid WriteCommentReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        checkWritePermission(board, user);
        Post post = postProvider.findPost(board, req.getPostId());

        Comment parentComment = null;

        // 작성하려는 댓글이 대댓글 이라면
        if (req.getDepth() > 0 || req.getParentId() != 0) {
            parentComment = commentProvider.findComment(post, req.getParentId());
            if (parentComment.getDepth() != req.getDepth() - 1) throw new NoSuchCommentException();
        }

        Comment newComment = Comment.create(
                commentProvider.getNewCommentId(post),
                post,
                user,
                req.getDepth(),
                parentComment,
                req.getContent(),
                req.getAnonymous());
        commentRepository.save(newComment);
        post.increaseTotalComments();
    }

    @Transactional
    public void deleteComment(User user, DeleteCommentReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        board.checkAccessibleRole(user);
        Post post = postProvider.findPost(board, req.getPostId());
        Comment comment = commentProvider.findComment(post, req.getCommentId());
        checkCommentDeletable(comment, user);

        comment.delete();
        post.decreaseTotalComments();
    }

    public List<CommentRes> viewCommentTree(User nullableUser, FindCommentTreeReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        checkViewPermission(board, nullableUser);
        Post post = postProvider.findPost(board, req.getPostId());

        return commentProvider.findCommentTree(post).stream()
                .map(comment -> CommentRes.create(nullableUser, comment, anonymousUserIdProvider))
                .toList();
    }

    private void checkCommentDeletable(Comment comment, User user) {
        comment.getBoard().checkAccessibleRole(user);
        if (!comment.hasPermission(user)) throw new DoNotHavePermissionToDeleteCommentException();
    }

    private void checkViewPermission(Board board, User nullableUser) {
        board.checkAccessibleRole(nullableUser);
        if (!board.isPublicComment() && nullableUser == null) throw new UnAuthorizedException();
    }

    public void checkWritePermission(Board board, User user) {
        board.checkAccessibleRole(user);
        if (board.getWriteCommentLevel().getValue() > user.getLevel().getValue()) throw new DoNotHavePermissionToWriteCommentOnBoardException();
    }

}
