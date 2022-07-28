package bssm.bsm.board.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Entity
@Table
public class PostCategory {

    @EmbeddedId
    private PostCategoryId id;

    @Column(nullable = false, length = 10)
    private String name;
}
