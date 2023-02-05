package bssm.bsm.domain.board.emoticon.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
@NoArgsConstructor
public class EmoticonItemPk implements Serializable {

    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Integer idx;

    @EqualsAndHashCode.Include
    @Column
    private Long emoticonId;

    public static EmoticonItemPk create(Integer idx, Emoticon emoticon) {
        EmoticonItemPk emoticonItemPk = new EmoticonItemPk();
        emoticonItemPk.idx = idx;
        emoticonItemPk.emoticonId = emoticon.getId();
        return emoticonItemPk;
    }
}
