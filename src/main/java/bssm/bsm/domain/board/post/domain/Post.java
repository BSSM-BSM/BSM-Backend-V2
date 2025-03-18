package bssm.bsm.domain.board.post.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.like.domain.type.LikeType;
import bssm.bsm.domain.board.post.domain.type.PostAnonymousConverter;
import bssm.bsm.domain.board.post.domain.type.PostAnonymousType;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.global.entity.BaseTimeEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.SQLRestriction;

import java.util.Objects;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "category_id")
    private String categoryId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "board_id", referencedColumnName = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private PostCategory category;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("0")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    @ColumnDefault("0")
    private int viewCount;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    @ColumnDefault("0")
    private int commentCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int likeCount;

    @Convert(converter = PostAnonymousConverter.class)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private PostAnonymousType anonymousType;

    public static Post create(Board board, User writer, String title,
                              String content, PostAnonymousType anonymousType, PostCategory category) {
        Post post = new Post();
        post.board = board;
        post.writer = writer;
        post.title = title;
        post.content = content;
        post.isDeleted = false;
        post.viewCount = 0;
        post.commentCount = 0;
        post.likeCount = 0;
        post.setCategory(category);
        post.setAnonymousType(anonymousType);
        return post;
    }

    public void update(String title, String content, PostCategory category, PostAnonymousType anonymous) {
        this.title = title;
        this.content = content;
        setCategory(category);
        setAnonymousType(anonymous);
    }

    private void setCategory(PostCategory category) {
        this.category = category;
        this.categoryId = category == null ? null : category.getPk().getId();
    }

    private void setAnonymousType(PostAnonymousType anonymousType) {
        this.anonymousType = anonymousType;
        if (anonymousType == PostAnonymousType.NO_RECORD) {
            this.writer = null;
        }
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

    public void applyPostLike() {
        this.likeCount++;
    }

    public void applyPostDislike() {
        this.likeCount--;
    }

    public void cancelPostLike(LikeType prevLike) {
        this.likeCount -= prevLike.getValue();
    }

    public void reservePostLike(LikeType prevLike) {
        this.likeCount -= (prevLike.getValue() * 2);
    }

    public boolean hasPermission(User user) {
        if (user.getLevel() == UserLevel.ADMIN) {
            return true;
        }
        if (this.anonymousType == PostAnonymousType.NO_RECORD
                || writer == null) {
            return false;
        }
        return Objects.equals(writer.getId(), user.getId());
    }

}
