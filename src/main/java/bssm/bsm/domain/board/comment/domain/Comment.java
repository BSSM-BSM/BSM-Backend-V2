package bssm.bsm.domain.board.comment.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.comment.domain.type.CommentAnonymousConverter;
import bssm.bsm.domain.board.comment.domain.type.CommentAnonymousType;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserLevel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
            @JoinColumn(name = "board_id", referencedColumnName = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "post_id", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_code")
    private User writer;

    @Column(name = "isDelete", nullable = false)
    @ColumnDefault("0")
    private boolean delete;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private int depth;

    @Column(name = "parent_id", columnDefinition = "INT UNSIGNED")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "board_id", referencedColumnName = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "post_id", referencedColumnName = "post_id", insertable = false, updatable = false),
    })
    private Comment parent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Convert(converter = CommentAnonymousConverter.class)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private CommentAnonymousType anonymous;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    @OrderBy("id")
    private final Set<Comment> childComments = new HashSet<>();

    public static Comment create(long id, Post post, User writer, int depth,
                                 Comment parent, String content, CommentAnonymousType anonymous) {
        Comment comment = new Comment();
        comment.pk = CommentPk.create(id, post);
        comment.board = post.getBoard();
        comment.writer = writer;
        comment.delete = false;
        comment.depth = depth;
        comment.content = content;
        comment.createdAt = LocalDateTime.now();
        if (parent != null) {
            comment.parentId = parent.getPk().getId();
        }
        comment.setAnonymous(anonymous);
        return comment;
    }

    private void setAnonymous(CommentAnonymousType anonymous) {
        this.anonymous = anonymous;
        if (anonymous == CommentAnonymousType.NO_RECORD) {
            this.writer = null;
        }
    }

    public void delete() {
        this.delete = true;
    }

    public boolean hasPermission(User user) {
        if (user.getLevel() == UserLevel.ADMIN) {
            return true;
        }
        if (this.anonymous == CommentAnonymousType.NO_RECORD
                || writer == null) {
            return false;
        }
        return Objects.equals(writer.getCode(), user.getCode());
    }
}
