package bssm.bsm.domain.board.post.domain;

import bssm.bsm.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "post_log", timeToLive = 86400)
public class PostTempLog implements Serializable {

    @Id
    private String id;

    private String boardId;
    private Long postId;
    private String categoryId;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostTempLog create(Post post, User writer) {
        PostTempLog log = new PostTempLog();
        String boardId = post.getBoard().getId();
        long postId = post.getId();

        log.id = boardId + ":" + postId;
        log.boardId = boardId;
        log.postId = postId;
        log.categoryId = post.getCategoryId();
        log.userId = writer.getId();
        log.title = post.getTitle();
        log.content = post.getContent();
        log.createdAt = post.getCreatedAt();
        log.modifiedAt = LocalDateTime.now();
        return log;
    }

}
