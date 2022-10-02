package bssm.bsm.domain.board.emoticon.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@Embeddable
public class EmoticonItemPk implements Serializable {

    @Column(columnDefinition = "INT UNSIGNED")
    private int idx;

    @ManyToOne
    private Emoticon emoticon;

    @Builder
    public EmoticonItemPk(int idx, Emoticon emoticon) {
        this.idx = idx;
        this.emoticon = emoticon;
    }
}
