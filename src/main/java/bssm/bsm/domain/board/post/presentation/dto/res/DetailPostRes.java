package bssm.bsm.domain.board.post.presentation.dto.res;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DetailPostRes extends PostRes {

    private String content;
    private boolean permission;
    private int like;
    private boolean anonymous;
}
