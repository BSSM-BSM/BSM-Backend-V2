package bssm.bsm.domain.board.category.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.presentation.dto.res.PostCategoryRes;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategory {

    @EmbeddedId
    @JoinColumns({
            @JoinColumn(name = "categoryId", insertable = false, updatable = false),
            @JoinColumn(name = "boardId", insertable = false, updatable = false)
    })
    private PostCategoryPk pk;

    @ManyToOne
    @JoinColumn(name = "boardId", insertable = false, updatable = false)
    private Board board;

    @Column(nullable = false, length = 10)
    private String name;

    @Builder
    public PostCategory(PostCategoryPk pk, Board board, String name) {
        this.pk = pk;
        this.board = board;
        this.name = name;
    }

    public PostCategoryRes toResponse() {
        return PostCategoryRes.builder()
                .id(pk.getId())
                .name(name)
                .build();
    }
}
