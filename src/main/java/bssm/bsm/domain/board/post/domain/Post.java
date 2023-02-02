package bssm.bsm.domain.board.post.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserLevel;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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

    @ManyToOne
    @JoinColumn(name = "board_id")
    @MapsId("boardId")
    private Board board;

    @Column(name = "category_id")
    private String categoryId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "category_id", insertable = false, updatable = false)
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

    @Builder
    public Post(PostPk pk, Board board, PostCategory category, boolean delete, User writer, String title, String content, int view, int totalComments, int totalLikes, boolean anonymous, Date createdAt) {
        this.pk = pk;
        this.board = board;
        this.delete = delete;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.view = view;
        this.totalComments = totalComments;
        this.totalLikes = totalLikes;
        this.anonymous = anonymous;
        this.createdAt = createdAt;

        setCategory(category);
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

    public void increaseViewCnt() {
        this.view++;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public boolean checkPermission(User user) {
        return Objects.equals(writer.getCode(), user.getCode()) || user.getLevel() == UserLevel.ADMIN;
    }

}
