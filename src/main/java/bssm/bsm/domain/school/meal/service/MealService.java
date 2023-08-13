package bssm.bsm.domain.school.meal.service;

import bssm.bsm.domain.school.meal.domain.MealRepository;
import bssm.bsm.domain.school.meal.presentation.dto.res.MealRes;
import bssm.bsm.domain.school.meal.domain.Meal;
import bssm.bsm.domain.school.meal.facade.MealFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealFacade mealFacade;
    private final MealProvider mealProvider;
    private final MealRepository mealRepository;

    public MealRes getMeal(LocalDate date) {
         List<Meal> mealList = mealFacade.getMealList(date);
         return MealRes.create(mealList);
    }

    public void updateMonthMeal(YearMonth date) throws IOException {
        List<Meal> mealList = mealProvider.getRawMonthMealList(date).stream()
                .map(meal -> meal.toEntity(mealFacade.filterMealStr(meal.getDDISH_NM())))
                .toList();

        List<Meal> deleteList = mealRepository.findAllByPkDateBetween(date.atDay(1), date.atEndOfMonth());
        mealList.forEach(deleteList::remove);
        mealRepository.deleteAll(deleteList);

        mealRepository.saveAll(mealList);
    }

}
