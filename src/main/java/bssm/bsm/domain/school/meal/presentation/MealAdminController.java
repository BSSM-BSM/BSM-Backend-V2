package bssm.bsm.domain.school.meal.presentation;

import bssm.bsm.domain.school.meal.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.YearMonth;

@RestController
@RequestMapping("admin/meal")
@RequiredArgsConstructor
public class MealAdminController {

    private final MealService mealService;

    @PutMapping("{date}")
    public void updateMonthMeal(@PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth date) throws IOException {
        mealService.updateMonthMeal(date);
    }

}
