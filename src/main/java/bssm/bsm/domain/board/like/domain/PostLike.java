package bssm.bsm.domain.board.like.domain;

import bssm.bsm.domain.user.domain.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @EmbeddedId
    private PostLikePk pk;

    @Column(columnDefinition = "INT UNSIGNED")
    private Long userCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userCode", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @Column(name = "is_like", nullable = false, columnDefinition = "tinyint")
    private int like;

    @Builder
    public PostLike(PostLikePk pk, Long userCode, User user, int like) {
        this.pk = pk;
        this.userCode = userCode;
        this.user = user;
        this.like = like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
