package bssm.bsm.domain.board.like.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @EmbeddedId
    private PostLikePk pk;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @MapsId("boardId")
    private Board board;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "post_id", insertable = false, updatable = false),
            @JoinColumn(name = "board_id", insertable = false, updatable = false)
    })
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_code")
    private User user;

    @Column(name = "is_like", nullable = false, columnDefinition = "tinyint")
    private int like;

    public static PostLike create(long likeId, Post post, User user, int like) {
        PostLikePk postLikePk = PostLikePk.create(likeId, post);
        PostLike postLike = new PostLike();
        postLike.pk = postLikePk;
        postLike.board = post.getBoard();
        postLike.user = user;
        postLike.like = like;
        return postLike;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
