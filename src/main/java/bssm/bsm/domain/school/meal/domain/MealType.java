package bssm.bsm.domain.school.meal.domain;

import bssm.bsm.domain.school.meal.exception.ParseMealTypeException;

public enum MealType {
    MORNING,
    LUNCH,
    DINNER;

    public static MealType create(String type) {
        return switch (type) {
            case "조식" -> MealType.MORNING;
            case "중식" -> MealType.LUNCH;
            case "석식" -> MealType.DINNER;
            default -> throw new ParseMealTypeException();
        };
    }
}
