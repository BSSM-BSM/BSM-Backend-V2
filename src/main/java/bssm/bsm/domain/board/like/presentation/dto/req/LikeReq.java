package bssm.bsm.domain.board.like.presentation.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor
public class LikeReq {

    @Min(-1) @Max(1)
    int like;
}
