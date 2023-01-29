package bssm.bsm.domain.board.category.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PostCategoryPk implements Serializable {

    @Column(length = 16)
    private String id;

    @Column
    private String boardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostCategoryPk)) return false;

        PostCategoryPk that = (PostCategoryPk) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return boardId != null ? boardId.equals(that.boardId) : that.boardId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (boardId != null ? boardId.hashCode() : 0);
        return result;
    }
}
