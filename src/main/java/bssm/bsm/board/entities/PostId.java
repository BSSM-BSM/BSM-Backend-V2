package bssm.bsm.board.entities;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class PostId implements Serializable {

    @Column(columnDefinition = "INT UNSIGNED")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Board board;
}
