package bssm.bsm.board.post.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Embeddable
public class PostId implements Serializable {

    @Column(columnDefinition = "INT UNSIGNED")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Board board;
}
