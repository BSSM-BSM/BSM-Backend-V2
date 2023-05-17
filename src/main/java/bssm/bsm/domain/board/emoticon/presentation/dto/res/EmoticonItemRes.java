package bssm.bsm.domain.board.emoticon.presentation.dto.res;

import bssm.bsm.domain.board.emoticon.domain.EmoticonItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmoticonItemRes {

    private long idx;
    private String type;

    public static EmoticonItemRes create(EmoticonItem item) {
        EmoticonItemRes res = new EmoticonItemRes();
        res.idx = item.getPk().getIdx();
        res.type = item.getType();
        return res;
    }
}
