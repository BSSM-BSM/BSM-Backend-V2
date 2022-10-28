package bssm.bsm.domain.school.meal.service;

import bssm.bsm.domain.school.meal.domain.MealRepository;
import bssm.bsm.domain.school.meal.presentation.dto.response.MealResponse;
import bssm.bsm.domain.school.meal.domain.Meal;
import bssm.bsm.domain.school.meal.facade.MealFacade;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealFacade mealFacade;

    public MealResponse getMeal(LocalDate date) {
         List<Meal> mealList = mealFacade.getMealList(date);
         if (mealList.isEmpty()) throw new NotFoundException();

         return mealFacade.toResponse(mealList);
    }

}
