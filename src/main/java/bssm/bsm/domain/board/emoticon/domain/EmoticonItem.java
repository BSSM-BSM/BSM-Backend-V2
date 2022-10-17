package bssm.bsm.domain.board.emoticon.domain;

import bssm.bsm.domain.board.emoticon.presentation.dto.response.EmoticonItemResponse;
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

    @Column(length = 4)
    private String type;

    public EmoticonItemResponse toDto() {
        return EmoticonItemResponse.builder()
                .id(pk.getEmoticon().getId())
                .idx(pk.getIdx())
                .type(type)
                .build();
    }

    @Builder
    public EmoticonItem(EmoticonItemPk pk, String type) {
        this.pk = pk;
        this.type = type;
    }
}
