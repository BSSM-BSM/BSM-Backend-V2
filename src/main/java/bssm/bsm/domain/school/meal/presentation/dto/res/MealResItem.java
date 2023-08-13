package bssm.bsm.domain.school.meal.presentation.dto.res;

import bssm.bsm.domain.school.meal.domain.Meal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MealResItem {

    private String content;
    private float cal;

    public static MealResItem create(Meal meal) {
        MealResItem resItem = new MealResItem();
        resItem.content = meal.getContent();
        resItem.cal = meal.getCal();
        return resItem;
    }
}
