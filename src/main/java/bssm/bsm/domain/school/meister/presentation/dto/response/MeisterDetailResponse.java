package bssm.bsm.domain.school.meister.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeisterDetailResponse {

    private String scoreHtmlContent;
    private String pointHtmlContent;
    private float score;
    private int positivePoint;
    private int negativePoint;
}
