package bssm.bsm.board.post.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PostCategoryPk implements Serializable {

    @Column(length = 10)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Board board;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        PostCategoryPk other = (PostCategoryPk)o;

        return other.board == board && Objects.equals(other.id, id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, id);
    }
}
