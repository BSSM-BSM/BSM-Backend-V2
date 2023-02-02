package bssm.bsm.domain.board.like.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeRes {

    private int like;
    private int totalLikes;
}
