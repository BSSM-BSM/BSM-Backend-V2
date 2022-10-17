package bssm.bsm.domain.board.like.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor
public class LikeRequest {

    @Min(-1) @Max(1)
    int like;
}
