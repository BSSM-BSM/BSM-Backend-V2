package bssm.bsm.domain.board.like.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponse {

    private int like;
    private int totalLikes;
}