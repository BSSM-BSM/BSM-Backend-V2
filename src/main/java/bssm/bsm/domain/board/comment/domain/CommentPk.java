package bssm.bsm.domain.board.comment.domain;

import bssm.bsm.domain.board.post.domain.Post;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class CommentPk implements Serializable {

    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private long id;

    @EqualsAndHashCode.Include
    @Column
    private String boardId;

    @EqualsAndHashCode.Include
    @Column(name = "post_id")
    private Long postId;

    public static CommentPk create(long id, Post post) {
        CommentPk commentPk = new CommentPk();
        commentPk.id = id;
        commentPk.boardId = post.getPk().getBoardId();
        commentPk.postId = post.getPk().getId();
        return commentPk;
    }
}
