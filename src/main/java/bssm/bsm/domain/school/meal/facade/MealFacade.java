package bssm.bsm.domain.school.meal.facade;

import bssm.bsm.domain.school.meal.entities.Meal;
import bssm.bsm.domain.school.meal.entities.MealType;
import bssm.bsm.domain.school.meal.repositories.MealRepository;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MealFacade {

    private final MealRepository mealRepository;

    public Meal getMeal(LocalDate date) {
        return mealRepository.findById(date).orElseThrow(() -> {throw new NotFoundException();});
    }

    public String getMealStr(MealType type) {
        Meal meal = getMeal(LocalDate.now());
        return switch (type) {
            case MORNING -> meal.getMorningForFacade();
            case LUNCH -> meal.getLunchForFacade();
            case DINNER -> meal.getDinnerForFacade();
        };
    }

}
