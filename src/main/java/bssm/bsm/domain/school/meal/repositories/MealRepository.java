package bssm.bsm.domain.school.meal.repositories;

import bssm.bsm.domain.school.meal.entities.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MealRepository extends JpaRepository<Meal, LocalDate> {}
