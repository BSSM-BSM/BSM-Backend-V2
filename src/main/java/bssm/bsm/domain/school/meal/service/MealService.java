package bssm.bsm.domain.school.meal.service;

import bssm.bsm.domain.school.meal.presentation.dto.response.MealResponse;
import bssm.bsm.domain.school.meal.domain.Meal;
import bssm.bsm.domain.school.meal.facade.MealFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealFacade mealFacade;

    public MealResponse getMeal(LocalDate date) {
         Meal meal = mealFacade.getMeal(date);
         return MealResponse.builder()
                 .morning(meal.getMorning())
                 .lunch(meal.getLunch())
                 .dinner(meal.getDinner())
                 .build();
    }

}
