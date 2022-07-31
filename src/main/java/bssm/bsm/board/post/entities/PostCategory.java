package bssm.bsm.board.post.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class PostCategory {

    @EmbeddedId
    private PostCategoryId id;

    @Column(nullable = false, length = 10)
    private String name;
}
