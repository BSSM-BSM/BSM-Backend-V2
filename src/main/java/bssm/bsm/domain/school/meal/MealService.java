package bssm.bsm.domain.school.meal;

import bssm.bsm.domain.school.meal.entities.Meal;
import bssm.bsm.domain.school.meal.repositories.MealRepository;
import bssm.bsm.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    public Meal getMeal(LocalDate date) {
        return mealRepository.findById(date).orElseThrow(
                () -> {throw new NotFoundException();}
        );
    }
}
