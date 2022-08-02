package bssm.bsm.board.comment.entity;

import bssm.bsm.board.post.entities.Post;
import bssm.bsm.board.post.entities.PostPk;
import bssm.bsm.user.entities.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Comment {

    @EmbeddedId
    private CommentPk commentPk;

    @Column(name = "isDelete", nullable = false)
    @ColumnDefault("0")
    private boolean delete;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private int usercode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usercode", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @Column(nullable = false)
    private boolean haveChild;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private int depth;

    @Column(columnDefinition = "INT UNSIGNED")
    private Integer parentId;

    // 대댓글의 깊이가 너무 깊으면 부모 댓글을 연쇄적으로 가져와 부하가 발생하기 때문에
    // 다른 방법으로 가져와야 됨
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "parentId", insertable = false, updatable = false),
            @JoinColumn(name = "boardId", insertable = false, updatable = false),
            @JoinColumn(name = "postId", insertable = false, updatable = false)
    })
    private Comment parent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private Date createdAt;
}
