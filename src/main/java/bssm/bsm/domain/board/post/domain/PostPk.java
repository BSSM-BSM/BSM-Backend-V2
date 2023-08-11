package bssm.bsm.domain.board.post.domain;

import bssm.bsm.domain.board.board.domain.Board;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
@NoArgsConstructor
public class PostPk implements Serializable {

    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private long id;

    @EqualsAndHashCode.Include
    @Column
    private String boardId;

    public static PostPk create(long id, Board board) {
        PostPk postPk = new PostPk();
        postPk.id = id;
        postPk.boardId = board.getId();
        return postPk;
    }
}
