package bssm.bsm.school.meister.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeisterScoreAndPointResponseDto {

    private String scoreHtmlContent;
    private String pointHtmlContent;
    private int positivePoint;
    private int negativePoint;
}
