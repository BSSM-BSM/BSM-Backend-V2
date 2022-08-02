package bssm.bsm.board.comment;

import bssm.bsm.board.comment.dto.request.WriteCommentDto;
import bssm.bsm.board.comment.entity.Comment;
import bssm.bsm.board.comment.entity.CommentPk;
import bssm.bsm.board.comment.repository.CommentRepository;
import bssm.bsm.board.post.dto.request.PostIdDto;
import bssm.bsm.board.post.entities.Board;
import bssm.bsm.board.post.entities.Post;
import bssm.bsm.board.post.entities.PostPk;
import bssm.bsm.board.post.repositories.PostRepository;
import bssm.bsm.board.utils.BoardUtil;
import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final BoardUtil boardUtil;

    @Transactional
    public void writeComment(User user, PostIdDto postIdDto, WriteCommentDto dto) {
        Board board = boardUtil.getBoard(postIdDto.getBoard());
        PostPk postPk = PostPk.builder()
                .id(postIdDto.getPostId())
                .board(board)
                .build();
        Post post = postRepository.findByPostPkAndDelete(postPk, false).orElseThrow(
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

        Comment comment = Comment.builder()
                .usercode(user.getUsercode())
                .depth(dto.getDepth())
                .parent(parentComment)
                .content(dto.getContent())
                .build();
        commentRepository.insertComment(comment, board.getId(), postIdDto.getPostId());
    }
}
