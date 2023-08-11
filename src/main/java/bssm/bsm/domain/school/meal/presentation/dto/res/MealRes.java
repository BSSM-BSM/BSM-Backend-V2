package bssm.bsm.domain.school.meal.presentation.dto.res;

import bssm.bsm.domain.school.meal.domain.Meal;
import bssm.bsm.domain.school.meal.domain.MealType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class MealRes {

    private final Map<MealType, MealResItem> data = new HashMap<>();
    private List<MealType> keys;

    public static MealRes create(List<Meal> mealList) {
        MealRes res = new MealRes();
        mealList.forEach(meal ->
                res.data.put(meal.getType(), MealResItem.create(meal))
        );
        res.keys = mealList.stream()
                .map(Meal::getType)
                .toList();
        return res;
    }
}
