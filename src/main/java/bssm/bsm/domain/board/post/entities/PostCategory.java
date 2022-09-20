package bssm.bsm.domain.board.post.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategory {

    @EmbeddedId
    @JoinColumns({
            @JoinColumn(name = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    })
    private PostCategoryPk postCategoryPk;

    @Column(nullable = false, length = 10)
    private String name;

    @Builder
    public PostCategory(PostCategoryPk postCategoryPk, String name) {
        this.postCategoryPk = postCategoryPk;
        this.name = name;
    }

    public void setPostCategoryPk(PostCategoryPk postCategoryPk) {
        this.postCategoryPk = postCategoryPk;
    }
}
