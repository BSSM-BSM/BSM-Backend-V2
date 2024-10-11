package bssm.bsm.domain.school.meal.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meal {

    @EmbeddedId
    private MealPk pk;

    @Column(nullable = false, length = 7, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private MealType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private float cal;

    public static Meal create(LocalDate date, MealType type, String content, float cal) {
        Meal meal = new Meal();
        meal.pk = MealPk.create(date, type);
        meal.content = content;
        meal.cal = cal;
        return meal;
    }

    public void update(String content, float cal) {
        this.content = content;
        this.cal = cal;
    }

}
