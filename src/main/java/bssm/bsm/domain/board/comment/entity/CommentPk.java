package bssm.bsm.domain.board.comment.entity;

import bssm.bsm.domain.board.post.entities.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Embeddable
public class CommentPk implements Serializable {

    @Column(columnDefinition = "INT UNSIGNED")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "boardId", insertable = false, updatable = false),
            @JoinColumn(name = "postId", insertable = false, updatable = false)
    })
    private Post post;
}