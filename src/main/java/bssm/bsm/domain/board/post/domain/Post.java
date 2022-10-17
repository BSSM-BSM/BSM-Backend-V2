package bssm.bsm.domain.board.post.domain;

import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.UserLevel;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @EmbeddedId
    private PostPk pk;

    @Column(length = 10)
    private String categoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    })
    private PostCategory category;

    @Column(name = "isDelete", nullable = false)
    @ColumnDefault("0")
    private boolean delete;

    @Column(columnDefinition = "INT UNSIGNED")
    private Long userCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userCode", nullable = false, insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    @ColumnDefault("0")
    private int hit;

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

    @Builder
    public Post(PostPk pk, String categoryId, PostCategory category, boolean delete, Long userCode, User user, String title, String content, int hit, int totalComments, int totalLikes, boolean anonymous, Date createdAt) {
        this.pk = pk;
        this.categoryId = categoryId;
        this.category = category;
        this.delete = delete;
        this.userCode = userCode;
        this.user = user;
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.totalComments = totalComments;
        this.totalLikes = totalLikes;
        this.anonymous = anonymous;
        this.createdAt = createdAt;
    }

    public void setCategory(PostCategory category) {
        this.category = category;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean checkPermission(User user, Post post) {
        return Objects.equals(post.getUserCode(), user.getCode()) || user.getLevel() == UserLevel.ADMIN;
    }

}
