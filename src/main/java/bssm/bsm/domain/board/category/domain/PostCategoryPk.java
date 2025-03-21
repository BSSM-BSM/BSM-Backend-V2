package bssm.bsm.domain.board.category.domain;

import lombok.Builder;
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
public class PostCategoryPk implements Serializable {

    @EqualsAndHashCode.Include
    @Column(length = 16)
    private String id;

    @EqualsAndHashCode.Include
    @Column
    private String boardId;

    @Builder
    public PostCategoryPk(String id, String boardId) {
        this.id = id;
        this.boardId = boardId;
    }
}
