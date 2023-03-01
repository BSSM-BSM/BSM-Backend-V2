package bssm.bsm.domain.board.post.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.like.domain.type.Like;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserLevel;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
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

    @Column(nullable = false, name = "is_anonymous")
    private boolean anonymous;

    @CreatedDate
    private Date createdAt;

    public static Post create(long id, Board board, User writer, String title,
                              String content, boolean anonymous, PostCategory category) {
        Post post = new Post();
        post.pk = PostPk.create(id, board);
        post.board = board;
        post.writer = writer;
        post.title = title;
        post.content = content;
        post.anonymous = anonymous;
        post.delete = false;
        post.createdAt = new Date();
        post.view = 0;
        post.totalComments = 0;
        post.totalLikes = 0;
        post.setCategory(category);
        return post;
    }

    private void setCategory(PostCategory category) {
        this.category = category;
        this.categoryId = category == null ? null : category.getPk().getId();
    }

    public void update(String title, String content, PostCategory category, boolean anonymous) {
        this.title = title;
        this.content = content;
        setCategory(category);
        this.anonymous = anonymous;
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

    public void cancelPostLike(Like prevLike) {
        this.totalLikes -= prevLike.getValue();
    }

    public void reservePostLike(Like prevLike) {
        this.totalLikes -= (prevLike.getValue() * 2);
    }

    public boolean checkPermission(User user) {
        return Objects.equals(writer.getCode(), user.getCode()) || user.getLevel() == UserLevel.ADMIN;
    }

}
