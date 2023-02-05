package bssm.bsm.domain.board.emoticon.domain;

import bssm.bsm.domain.board.emoticon.presentation.dto.res.EmoticonItemRes;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmoticonItem {

    @EmbeddedId
    private EmoticonItemPk pk;

    @ManyToOne
    @JoinColumn(name = "emoticon_id")
    @MapsId("emoticonId")
    private Emoticon emoticon;

    @Column(length = 4)
    private String type;

    public EmoticonItemRes toResponse() {
        return EmoticonItemRes.builder()
                .id(emoticon.getId())
                .idx(pk.getIdx())
                .type(type)
                .build();
    }

    public static EmoticonItem create(Emoticon emoticon, int idx, String fileExt) {
        EmoticonItem emoticonItem = new EmoticonItem();
        emoticonItem.pk = EmoticonItemPk.create(idx, emoticon);
        emoticonItem.emoticon = emoticon;
        emoticonItem.type = fileExt;
        return emoticonItem;
    }
}
