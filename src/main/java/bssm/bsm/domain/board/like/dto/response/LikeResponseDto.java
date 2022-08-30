package bssm.bsm.domain.board.like.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponseDto {

    private int like;
    private int totalLikes;
}
