package bssm.bsm.domain.school.meal.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, MealPk> {

    List<Meal> findByPkDate(LocalDate date);

}
