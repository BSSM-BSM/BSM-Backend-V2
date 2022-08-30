package bssm.bsm.domain.board.post.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class PostCategory {

    @EmbeddedId
    @JoinColumns({
            @JoinColumn(name = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    })
    private PostCategoryPk postCategoryPk;

    @Column(nullable = false, length = 10)
    private String name;
}
