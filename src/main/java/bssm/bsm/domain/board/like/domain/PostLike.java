package bssm.bsm.domain.board.like.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.like.domain.type.LikeType;
import bssm.bsm.domain.board.like.domain.type.LikeConverter;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

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
            @JoinColumn(name = "board_id", referencedColumnName = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "post_id", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_code")
    private User user;

    @Convert(converter = LikeConverter.class)
    @Column(name = "is_like", nullable = false, columnDefinition = "tinyint")
    private LikeType like;

    public static PostLike create(long likeId, Post post, User user, LikeType like) {
        PostLikePk postLikePk = PostLikePk.create(likeId, post);
        PostLike postLike = new PostLike();
        postLike.pk = postLikePk;
        postLike.board = post.getBoard();
        postLike.user = user;
        postLike.like = like;
        return postLike;
    }

    public void updateLike(LikeType like) {
        this.like = like;
    }
}
