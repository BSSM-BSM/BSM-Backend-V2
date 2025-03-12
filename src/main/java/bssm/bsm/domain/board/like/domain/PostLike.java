package bssm.bsm.domain.board.like.domain;

import bssm.bsm.domain.board.like.domain.type.LikeType;
import bssm.bsm.domain.board.like.domain.type.LikeConverter;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_code")
    private User user;

    @Convert(converter = LikeConverter.class)
    @Column(name = "type", nullable = false, columnDefinition = "tinyint")
    private LikeType type;

    public static PostLike create(Post post, User user, LikeType type) {
        PostLike postLike = new PostLike();
        postLike.post = post;
        postLike.user = user;
        postLike.type = type;
        return postLike;
    }

    public void update(LikeType type) {
        this.type = type;
    }
}
