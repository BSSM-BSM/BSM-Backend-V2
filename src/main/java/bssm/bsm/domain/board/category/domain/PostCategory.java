package bssm.bsm.domain.board.category.domain;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.presentation.dto.res.PostCategoryRes;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategory {

    @EmbeddedId
    private PostCategoryPk pk;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @MapsId("boardId")
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
