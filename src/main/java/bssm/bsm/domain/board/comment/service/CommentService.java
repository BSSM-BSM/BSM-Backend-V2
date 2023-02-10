package bssm.bsm.domain.board.comment.service;

import bssm.bsm.domain.board.anonymous.service.AnonymousUserIdProvider;
import bssm.bsm.domain.board.comment.exception.DoNotHavePermissionToDeleteCommentException;
import bssm.bsm.domain.board.comment.exception.DoNotHavePermissionToWriteCommentOnBoardException;
import bssm.bsm.domain.board.comment.presentation.dto.req.WriteCommentReq;
import bssm.bsm.domain.board.comment.presentation.dto.res.CommentRes;
import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.board.comment.domain.repository.CommentRepository;
import bssm.bsm.domain.board.post.presentation.dto.req.PostReq;
import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.post.service.PostProvider;
import bssm.bsm.global.error.exceptions.NotFoundException;
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
public class CommentService {

    private final CommentProvider commentProvider;
    private final BoardProvider boardProvider;
    private final PostProvider postProvider;
    private final AnonymousUserIdProvider anonymousUserIdProvider;

    private final CommentRepository commentRepository;

    @Transactional
    public void writeComment(User user, PostReq postReq, @Valid WriteCommentReq req) {
        Board board = boardProvider.findBoard(postReq.getBoardId());
        checkWritePermission(board, user);
        Post post = postProvider.findPost(board, postReq.getPostId());

        Comment parentComment = null;

        // 작성하려는 댓글이 대댓글 이라면
        if (req.getDepth() > 0 || req.getParentId() != 0) {
            // 대댓글이면 부모 댓글이 이미 게시글과 연결되어있기 때문에
            // 존재하는 게시글인지 굳이 확인할 필요가 없음
            parentComment = commentProvider.findComment(post, req.getParentId());
            if (parentComment.getDepth() != req.getDepth() - 1) throw new NotFoundException("부모 댓글을 찾을 수 없습니다");
            // 부모 댓글로 설정되어 있지 않으면 설정함
            if (!parentComment.isHaveChild()) {
                parentComment.setHaveChild(true);
                commentRepository.save(parentComment);
            }
        }

        Comment newComment = Comment.create(
                commentProvider.getNewCommentId(post),
                post,
                user,
                req.getDepth(),
                parentComment,
                req.getContent(),
                req.isAnonymous());
        commentRepository.save(newComment);
        post.setTotalComments(post.getTotalComments() + 1);
    }

    @Transactional
    public void deleteComment(User user, PostReq req, int commentId) {
        Board board = boardProvider.findBoard(req.getBoardId());
        board.checkAccessibleRole(user);
        Post post = postProvider.findPost(board, req.getPostId());


        Comment comment = commentProvider.findComment(post, commentId);
        checkCommentDeletable(comment, user);

        comment.setDelete(true);
        post.setTotalComments(post.getTotalComments() - 1);
    }

    public List<CommentRes> viewCommentList(Optional<User> user, PostReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        checkViewPermission(board, user);
        Post post = postProvider.findPost(board, req.getPostId());

        return commentTree(user, 0, commentProvider.findCommentList(post));
    }

    private List<CommentRes> commentTree(Optional<User> user, int depth, List<Comment> commentList) {
        List<CommentRes> commentDtoList = new ArrayList<>();
        List<Comment> deleteList = new ArrayList<>();

        for (Iterator<Comment> iterator = commentList.iterator(); iterator.hasNext();) {
            Comment comment = iterator.next();
            // 만약 최적화를 위해 삭제 예정 리스트에 들어있는 댓글이라면
            if (deleteList.contains(comment)) {
                deleteList.remove(comment);
                iterator.remove();
                continue;
            }

            // 대댓글의 깊이가 현재 불러오려는 깊이와 같은지 확인
            if (comment.getDepth() != depth) {
                continue;
            }

            CommentRes commentRes = CommentRes.create(user, comment, anonymousUserIdProvider);

            // 자식 댓글이 있다면
            if (comment.isHaveChild()) {
                List<Comment> childList = new ArrayList<>();

                commentList.forEach(child -> {
                    if (child.getDepth() == depth + 1) { // 자식의 댓글의 깊이가 바로 밑이라면
                        if (child.getParentId() == comment.getPk().getId()) { // 자식 댓글의 부모가 현재 댓글이라면
                            childList.add(child);
                            // 해당 자식 댓글은 최적화를 위해 나중에 삭제할 댓글 리스트에 추가
                            deleteList.add(child);
                        }
                    } else { // 아니라면 자식 댓글의 자식 댓글일 수도 있으니 일단 넣음
                        childList.add(child);
                    }
                });

                commentRes.setChild(commentTree(user, depth+1, childList));
            }
            commentDtoList.add(commentRes);

            // 해당 댓글은 처리가 완료되었으므로 최적화를 위해 리스트에서 제외
            iterator.remove();
        }

        return commentDtoList;
    }

    private void checkCommentDeletable(Comment comment, User user) {
        comment.getBoard().checkAccessibleRole(user);
        if (!comment.checkPermission(user)) throw new DoNotHavePermissionToDeleteCommentException();
    }

    private void checkViewPermission(Board board, Optional<User> user) {
        board.checkAccessibleRole(user);
        if (!board.isPublicComment() && user.isEmpty()) throw new UnAuthorizedException();
    }

    public void checkWritePermission(Board board, User user) {
        board.checkAccessibleRole(user);
        if (board.getWriteCommentLevel().getValue() > user.getLevel().getValue()) throw new DoNotHavePermissionToWriteCommentOnBoardException();
    }

}
