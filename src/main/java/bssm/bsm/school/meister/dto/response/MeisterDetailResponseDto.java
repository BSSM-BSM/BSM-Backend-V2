package bssm.bsm.school.meister.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeisterDetailResponseDto {

    private String scoreHtmlContent;
    private String pointHtmlContent;
    private int score;
    private int positivePoint;
    private int negativePoint;
}
