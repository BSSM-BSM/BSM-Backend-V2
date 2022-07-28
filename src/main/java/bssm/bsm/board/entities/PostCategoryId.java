package bssm.bsm.board.entities;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class PostCategoryId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Board board;

    @Column(length = 10)
    private String category;
}
