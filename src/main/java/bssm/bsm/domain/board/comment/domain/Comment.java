package bssm.bsm.domain.board.comment.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserLevel;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @EmbeddedId
    private CommentPk pk;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @MapsId("boardId")
    private Board board;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "post_id", insertable = false, updatable = false)
    })
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_code")
    private User writer;

    @Column(name = "isDelete", nullable = false)
    @ColumnDefault("0")
    private boolean delete;

    @Column(nullable = false)
    private boolean haveChild;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private int depth;

    @Column(name = "parent_id", columnDefinition = "INT UNSIGNED")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "post_id", insertable = false, updatable = false),
            @JoinColumn(name = "parent_id", insertable = false, updatable = false),
    })
    private Comment parent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, name = "is_anonymous")
    private boolean anonymous;

    @CreatedDate
    private Date createdAt;

    public static Comment create(long id, Post post, User writer, int depth, Comment parent, String content, boolean anonymous) {
        CommentPk commentPk = CommentPk.create(id, post);
        Comment comment = new Comment();
        comment.pk = commentPk;
        comment.board = post.getBoard();
        comment.writer = writer;
        comment.delete = false;
        comment.haveChild = false;
        comment.depth = depth;
        comment.content = content;
        comment.anonymous = anonymous;
        comment.createdAt = new Date();
        if (parent != null) {
            comment.parentId = parent.getPk().getId();
        }
        return comment;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setHaveChild(boolean haveChild) {
        this.haveChild = haveChild;
    }

    public boolean checkPermission(User user) {
        return Objects.equals(writer.getCode(), user.getCode()) || user.getLevel() == UserLevel.ADMIN;
    }
}
