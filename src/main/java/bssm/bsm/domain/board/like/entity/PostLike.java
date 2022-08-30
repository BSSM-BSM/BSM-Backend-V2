package bssm.bsm.domain.board.like.entity;

import bssm.bsm.domain.user.entities.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class PostLike {

    @EmbeddedId
    private PostLikePk postLikePk;

    @Column(columnDefinition = "INT UNSIGNED")
    private Long userCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userCode", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @Column(name = "is_like", nullable = false, columnDefinition = "tinyint")
    private int like;
}
