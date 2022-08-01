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
public class PostCategoryId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Board board;

    @Column(length = 10)
    private String categoryId;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        PostCategoryId other = (PostCategoryId)o;

        return other.board == board && Objects.equals(other.categoryId, categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, categoryId);
    }
}
