package bssm.bsm.domain.board.like.domain;

import bssm.bsm.domain.board.post.domain.Post;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
@NoArgsConstructor
public class PostLikePk implements Serializable {

    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private long id;

    @EqualsAndHashCode.Include
    @Column
    private String boardId;

    @EqualsAndHashCode.Include
    @Column(name = "post_id")
    private Long postId;

    public static PostLikePk create(long id, Post post) {
        PostLikePk postLikePk = new PostLikePk();
        postLikePk.id = id;
        postLikePk.boardId = post.getPk().getBoardId();
        postLikePk.postId = post.getPk().getId();
        return postLikePk;
    }
}
