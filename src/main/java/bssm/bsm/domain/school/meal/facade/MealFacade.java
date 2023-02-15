package bssm.bsm.domain.school.meal.facade;

import bssm.bsm.domain.school.meal.domain.Meal;
import bssm.bsm.domain.school.meal.domain.MealPk;
import bssm.bsm.domain.school.meal.domain.MealType;
import bssm.bsm.domain.school.meal.domain.MealRepository;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MealFacade {

    private final MealRepository mealRepository;

    public String filterMealStr(String str) {
        return str.replaceAll("<br/>|\\([0-9.]*?\\)|\\(산고\\)", "").trim();
    }

    public String getTodayMealStr(MealType type) {
        return getMeal(LocalDate.now(), type).getContent();
    }

    public List<Meal> getMealList(LocalDate date) {
        return mealRepository.findByPkDate(date);
    }

    public Meal getMeal(LocalDate date, MealType type) {
        return mealRepository.findById(MealPk.create(date, type))
                .orElseThrow(NotFoundException::new);
    }

}
