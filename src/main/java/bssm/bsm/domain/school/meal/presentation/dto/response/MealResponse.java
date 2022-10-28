package bssm.bsm.domain.school.meal.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class MealResponse {

    private MealResponseItem morning;
    private MealResponseItem lunch;
    private MealResponseItem dinner;

}
