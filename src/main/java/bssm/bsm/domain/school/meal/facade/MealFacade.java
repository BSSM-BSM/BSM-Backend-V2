package bssm.bsm.domain.school.meal.facade;

import bssm.bsm.domain.school.meal.domain.Meal;
import bssm.bsm.domain.school.meal.domain.MealPk;
import bssm.bsm.domain.school.meal.domain.MealType;
import bssm.bsm.domain.school.meal.domain.MealRepository;
import bssm.bsm.domain.school.meal.presentation.dto.response.MealResponse;
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

    public MealResponse toResponse(List<Meal> mealList) {
        MealResponse mealResponse = new MealResponse();
        mealList.forEach(meal -> {
            switch (meal.getPk().getType()) {
                case MORNING -> mealResponse.setMorning(meal.toResponseItem());
                case LUNCH -> mealResponse.setLunch(meal.toResponseItem());
                case DINNER -> mealResponse.setDinner(meal.toResponseItem());
            }
        });
        return mealResponse;
    }

    public List<Meal> getMealList(LocalDate date) {
        return mealRepository.findByPkDate(date);
    }

    public Meal getMeal(LocalDate date, MealType type) {
        return mealRepository.findById(new MealPk(date, type)).orElseThrow(NotFoundException::new);
    }

}
