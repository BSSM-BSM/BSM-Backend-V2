package bssm.bsm.domain.board.comment.entity;

import bssm.bsm.domain.user.entities.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @EmbeddedId
    private CommentPk pk;

    @Column(name = "isDelete", nullable = false)
    @ColumnDefault("0")
    private boolean delete;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long userCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userCode", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @Column(nullable = false)
    private boolean haveChild;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private int depth;

    @Column(columnDefinition = "INT UNSIGNED")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "parentId", insertable = false, updatable = false),
            @JoinColumn(name = "boardId", insertable = false, updatable = false),
            @JoinColumn(name = "postId", insertable = false, updatable = false)
    })
    private Comment parent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, name = "is_anonymous")
    private boolean anonymous;

    @CreatedDate
    private Date createdAt;

    @Builder
    public Comment(CommentPk pk, boolean delete, Long userCode, User user, boolean haveChild, int depth, Long parentId, Comment parent, String content, boolean anonymous, Date createdAt) {
        this.pk = pk;
        this.delete = delete;
        this.userCode = userCode;
        this.user = user;
        this.haveChild = haveChild;
        this.depth = depth;
        this.parentId = parentId;
        this.parent = parent;
        this.content = content;
        this.anonymous = anonymous;
        this.createdAt = createdAt;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setHaveChild(boolean haveChild) {
        this.haveChild = haveChild;
    }
}
