package bssm.bsm.domain.school.meal.domain;

import bssm.bsm.domain.school.meal.presentation.dto.response.MealResponseItem;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meal {

    @EmbeddedId
    private MealPk pk;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private float cal;

    @Builder
    public Meal(MealPk pk, String content, float cal) {
        this.pk = pk;
        this.content = content;
        this.cal = cal;
    }

    public MealResponseItem toResponseItem() {
        return new MealResponseItem(content, cal);
    }

}
