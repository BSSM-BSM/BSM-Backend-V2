package bssm.bsm.domain.board.emoticon.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmoticonItemPk implements Serializable {

    @Column(columnDefinition = "INT UNSIGNED")
    private long idx;

    @ManyToOne
    private Emoticon emoticon;
}
