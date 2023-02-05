package bssm.bsm.domain.board.emoticon.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmoticonItemRes {

    private long id;
    private long idx;
    private String type;
}
