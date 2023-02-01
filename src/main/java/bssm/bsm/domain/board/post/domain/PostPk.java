package bssm.bsm.domain.board.post.domain;

import lombok.*;

import javax.persistence.*;
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

    @Builder
    public PostPk(long id, String boardId) {
        this.id = id;
        this.boardId = boardId;
    }
}
