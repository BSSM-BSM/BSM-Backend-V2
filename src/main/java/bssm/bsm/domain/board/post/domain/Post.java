package bssm.bsm.domain.board.post.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.like.domain.type.LikeType;
import bssm.bsm.domain.board.post.domain.type.PostAnonymousConverter;
import bssm.bsm.domain.board.post.domain.type.PostAnonymousType;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserLevel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @EmbeddedId
    private PostPk pk;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @MapsId("boardId")
    private Board board;

    @Column(name = "category_id")
    private String categoryId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "board_id", referencedColumnName = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private PostCategory category;

    @Column(name = "isDelete", nullable = false)
    @ColumnDefault("0")
    private boolean delete;

    @ManyToOne
    @JoinColumn(name = "user_code")
    private User writer;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    @ColumnDefault("0")
    private int view;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    @ColumnDefault("0")
    private int totalComments;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int totalLikes;

    @Convert(converter = PostAnonymousConverter.class)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private PostAnonymousType anonymous;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static Post create(long id, Board board, User writer, String title,
                              String content, PostAnonymousType anonymous, PostCategory category) {
        Post post = new Post();
        post.pk = PostPk.create(id, board);
        post.board = board;
        post.writer = writer;
        post.title = title;
        post.content = content;
        post.delete = false;
        post.createdAt = LocalDateTime.now();
        post.view = 0;
        post.totalComments = 0;
        post.totalLikes = 0;
        post.setCategory(category);
        post.setAnonymous(anonymous);
        return post;
    }

    public void update(String title, String content, PostCategory category, PostAnonymousType anonymous) {
        this.title = title;
        this.content = content;
        setCategory(category);
        setAnonymous(anonymous);
    }

    private void setCategory(PostCategory category) {
        this.category = category;
        this.categoryId = category == null ? null : category.getPk().getId();
    }

    private void setAnonymous(PostAnonymousType anonymous) {
        this.anonymous = anonymous;
        if (anonymous == PostAnonymousType.NO_RECORD) {
            this.writer = null;
        }
    }

    public void delete() {
        this.delete = true;
    }

    public void increaseTotalViews() {
        this.view++;
    }

    public void increaseTotalComments() {
        this.totalComments++;
    }

    public void decreaseTotalComments() {
        this.totalComments--;
    }

    public void applyPostLike() {
        this.totalLikes++;
    }

    public void applyPostDislike() {
        this.totalLikes--;
    }

    public void cancelPostLike(LikeType prevLike) {
        this.totalLikes -= prevLike.getValue();
    }

    public void reservePostLike(LikeType prevLike) {
        this.totalLikes -= (prevLike.getValue() * 2);
    }

    public boolean hasPermission(User user) {
        if (user.getLevel() == UserLevel.ADMIN) {
            return true;
        }
        if (this.anonymous == PostAnonymousType.NO_RECORD
                || writer == null) {
            return false;
        }
        return Objects.equals(writer.getCode(), user.getCode());
    }

}
