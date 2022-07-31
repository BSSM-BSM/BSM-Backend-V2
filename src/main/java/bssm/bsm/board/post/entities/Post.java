package bssm.bsm.board.post.entities;

import bssm.bsm.user.entities.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Post {

    @EmbeddedId
    private PostId postId;

    @Column(nullable = false, length = 10)
    private String categoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "categoryId", insertable = false, updatable = false),
            @JoinColumn(name = "board_id", insertable = false, updatable = false)
    })
    private PostCategory category;

    @Column(name = "isDelete", nullable = false)
    @ColumnDefault("0")
    private boolean delete;

    @Column(columnDefinition = "INT UNSIGNED")
    private int usercode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usercode", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    @ColumnDefault("0")
    private int hit;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    @ColumnDefault("0")
    private int totalComments;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    @ColumnDefault("0")
    private int totalLikes;
}
