package bssm.bsm.domain.board.comment.domain;

import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "comment_log", timeToLive = 86400)
public class CommentTempLog {

    @Id
    private String id;

    private long postId;
    private long commentId;
    private long userCode;
    private Long parentId;
    private int depth;
    private String content;
    private LocalDateTime createdAt;

    public static CommentTempLog create(Comment comment, User writer) {
        CommentTempLog log = new CommentTempLog();
        long postId = comment.getPost().getId();
        long commentId = comment.getId();

        log.id = postId + ":" + commentId;
        log.postId = postId;
        log.commentId = commentId;
        log.userCode = writer.getCode();
        log.parentId = comment.getParent() != null ? comment.getParent().getId() : null;
        log.depth = comment.getDepth();
        log.content = comment.getContent();
        log.createdAt = comment.getCreatedAt();
        return log;
    }

}
