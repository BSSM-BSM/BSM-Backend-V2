package bssm.bsm.domain.board.comment.domain;

import bssm.bsm.domain.board.comment.domain.type.CommentAnonymousConverter;
import bssm.bsm.domain.board.comment.domain.type.CommentAnonymousType;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("0")
    private boolean isDeleted;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Convert(converter = CommentAnonymousConverter.class)
    @Column(name = "anonymous_type", nullable = false, columnDefinition = "INT UNSIGNED")
    private CommentAnonymousType anonymousType;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    @OrderBy("id")
    private final Set<Comment> childComments = new HashSet<>();

    public static Comment create(Post post, User writer, int depth,
                                 Comment parent, String content, CommentAnonymousType anonymous) {
        Comment comment = new Comment();
        comment.post = post;
        comment.writer = writer;
        comment.isDeleted = false;
        comment.depth = depth;
        comment.content = content;
        if (parent != null) {
            comment.parent = parent;
        }
        comment.setAnonymous(anonymous);
        return comment;
    }

    private void setAnonymous(CommentAnonymousType anonymousType) {
        this.anonymousType = anonymousType;
        if (anonymousType == CommentAnonymousType.NO_RECORD) {
            this.writer = null;
        }
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void updateNoRecordMode() {
        setAnonymous(CommentAnonymousType.NO_RECORD);
    }

    public boolean hasPermission(User user) {
        if (user.getLevel() == UserLevel.ADMIN) {
            return true;
        }
        if (this.anonymousType == CommentAnonymousType.NO_RECORD
                || writer == null) {
            return false;
        }
        return Objects.equals(writer.getId(), user.getId());
    }
}
