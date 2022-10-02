package bssm.bsm.domain.board.like.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class LikeRequestDto {

    @Min(-1) @Max(1)
    int like;
}
