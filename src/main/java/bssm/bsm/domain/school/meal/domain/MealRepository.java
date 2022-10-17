package bssm.bsm.domain.school.meal.domain;

import bssm.bsm.domain.school.meal.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MealRepository extends JpaRepository<Meal, LocalDate> {}
