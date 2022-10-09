package bssm.bsm.domain.board.emoticon.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmoticonItemResponse {

    private long id;
    private long idx;
    private String type;
}
