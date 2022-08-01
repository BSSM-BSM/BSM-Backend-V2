package bssm.bsm.board.like.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponseDto {

    private int like;
    private int totalLikes;
}
