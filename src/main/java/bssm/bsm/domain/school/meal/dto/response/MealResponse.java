package bssm.bsm.domain.school.meal.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MealResponse {

    private String morning;
    private String lunch;
    private String dinner;
}
