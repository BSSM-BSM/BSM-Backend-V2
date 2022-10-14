package bssm.bsm.domain.board.comment;

import bssm.bsm.domain.board.comment.dto.request.WriteCommentRequest;
import bssm.bsm.domain.board.comment.dto.response.CommentResponse;
import bssm.bsm.domain.board.comment.entity.Comment;
import bssm.bsm.domain.board.comment.entity.CommentPk;
import bssm.bsm.domain.board.comment.repository.CommentRepository;
import bssm.bsm.domain.board.post.dto.request.PostIdRequest;
import bssm.bsm.domain.board.post.entities.Board;
import bssm.bsm.domain.board.post.entities.Post;
import bssm.bsm.domain.board.post.entities.PostPk;
import bssm.bsm.domain.board.post.repositories.PostRepository;
import bssm.bsm.domain.board.utils.BoardUtil;
import bssm.bsm.domain.user.dto.response.UserResponseDto;
import bssm.bsm.domain.user.type.UserLevel;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import bssm.bsm.global.error.exceptions.NotFoundException;
import bssm.bsm.domain.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final BoardUtil boardUtil;

    @Transactional
    public void writeComment(User user, PostIdRequest postIdDto, @Valid WriteCommentRequest dto) {
        Board board = boardUtil.getBoardAndCheckRole(postIdDto.getBoard(), user.getRole());
        if (board.getWriteCommentLevel().getValue() > user.getLevel().getValue()) throw new ForbiddenException("권한이 없습니다");
        PostPk postPk = PostPk.builder()
                .id(postIdDto.getPostId())
                .board(board)
                .build();
        Post post = postRepository.findByPkAndDelete(postPk, false).orElseThrow(
                () -> {throw new NotFoundException("게시글을 찾을 수 없습니다");}
        );
        Comment parentComment = null;

        // 작성하려는 댓글이 대댓글 이라면
        if (dto.getDepth() > 0 || dto.getParentId() != 0) {
            // 대댓글이면 부모 댓글이 이미 게시글과 연결되어있기 때문에
            // 존재하는 게시글인지 굳이 확인할 필요가 없음
            parentComment = commentRepository.findById(
                    CommentPk.builder()
                            .id(dto.getParentId())
                            .post(post)
                            .build()
            ).orElseThrow(
                    () -> {throw new NotFoundException("부모 댓글을 찾을 수 없습니다");}
            );
            if (parentComment.getDepth() != dto.getDepth() - 1) throw new NotFoundException("부모 댓글을 찾을 수 없습니다");
            // 부모 댓글로 설정되어 있지 않으면 설정함
            if (!parentComment.isHaveChild()) {
                parentComment.setHaveChild(true);
                commentRepository.save(parentComment);
            }
        }

        Comment newComment = Comment.builder()
                .pk(
                        CommentPk.builder()
                                .id(commentRepository.countByPostPk(board.getId(), postIdDto.getPostId()) + 1)
                                .post(post)
                                .build()
                )
                .userCode(user.getCode())
                .depth(dto.getDepth())
                .parentId(parentComment == null? null: parentComment.getPk().getId())
                .content(dto.getContent())
                .anonymous(dto.isAnonymous())
                .build();
        commentRepository.save(newComment);
        post.setTotalComments(post.getTotalComments() + 1);
        postRepository.save(post);
    }

    public void deleteComment(User user, PostIdRequest postIdDto, int commnetId) {
        Board board = boardUtil.getBoardAndCheckRole(postIdDto.getBoard(), user.getRole());
        PostPk postPk = PostPk.builder()
                .id(postIdDto.getPostId())
                .board(board)
                .build();
        Post post = postRepository.findByPkAndDelete(postPk, false).orElseThrow(
                () -> {throw new NotFoundException("게시글을 찾을 수 없습니다");}
        );

        Comment comment = commentRepository.findById(
                CommentPk.builder()
                        .id(commnetId)
                        .post(post)
                        .build()
        ).orElseThrow(
                () -> {throw new NotFoundException("댓글을 찾을 수 없습니다");}
        );
        if (!checkPermission(user, comment)) throw new ForbiddenException("권한이 없습니다");

        comment.setDelete(true);
        commentRepository.save(comment);
        post.setTotalComments(post.getTotalComments() - 1);
        postRepository.save(post);
    }

    public List<CommentResponse> viewCommentList(User user, PostIdRequest postIdDto) {
        Board board = boardUtil.getBoardAndCheckRole(postIdDto.getBoard(), user.getRole());
        PostPk postPk = PostPk.builder()
                .id(postIdDto.getPostId())
                .board(board)
                .build();
        Post post = postRepository.findByPkAndDelete(postPk, false).orElseThrow(
                () -> {throw new NotFoundException("게시글을 찾을 수 없습니다");}
        );

        return commentTree(user, 0, commentRepository.findByPkPost(post));
    }

    private List<CommentResponse> commentTree(User user, int depth, List<Comment> commentList) {
        List<CommentResponse> commentDtoList = new ArrayList<>();
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

            CommentResponse commentDto = convertCommentDtoAndDeleteCheck(user, comment);

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

                commentDto.setChild(commentTree(user, depth+1, childList));
            }
            commentDtoList.add(commentDto);

            // 해당 댓글은 처리가 완료되었으므로 최적화를 위해 리스트에서 제외
            iterator.remove();
        }

        return commentDtoList;
    }

    private CommentResponse convertCommentDtoAndDeleteCheck(User user, Comment comment) {
        if (comment.isDelete()) {
            return CommentResponse.builder()
                    .id(comment.getPk().getId())
                    .isDelete(true)
                    .depth(comment.getDepth())
                    .permission(false)
                    .build();
        }
        return CommentResponse.builder()
                .id(comment.getPk().getId())
                .isDelete(false)
                .user(getUserData(comment.getUser(), comment.isAnonymous()))
                .createdAt(comment.getCreatedAt())
                .content(comment.getContent())
                .depth(comment.getDepth())
                .permission(checkPermission(user, comment))
                .build();
    }

    private boolean checkPermission(User user, Comment comment) {
        return Objects.equals(comment.getUserCode(), user.getCode()) || user.getLevel() == UserLevel.ADMIN;
    }

    private UserResponseDto getUserData(User user, boolean anonymous) {
        if (anonymous) {
            return UserResponseDto.builder()
                    .code((long) -1)
                    .nickname("ㅇㅇ")
                    .build();
        }
        return UserResponseDto.builder()
                .code(user.getCode())
                .nickname(user.getNickname())
                .build();
    }
}
