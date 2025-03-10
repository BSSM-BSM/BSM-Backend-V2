package bssm.bsm.domain.board.emoticon.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

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

    public static EmoticonItem create(Emoticon emoticon, int idx, String fileExt) {
        EmoticonItem emoticonItem = new EmoticonItem();
        emoticonItem.pk = EmoticonItemPk.create(idx, emoticon);
        emoticonItem.emoticon = emoticon;
        emoticonItem.type = fileExt;
        return emoticonItem;
    }
}
