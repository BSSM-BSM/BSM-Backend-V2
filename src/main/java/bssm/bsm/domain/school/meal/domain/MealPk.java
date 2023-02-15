package bssm.bsm.domain.school.meal.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
@NoArgsConstructor
public class MealPk implements Serializable {

    @EqualsAndHashCode.Include
    @Column
    private LocalDate date;

    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 7)
    @Enumerated(EnumType.STRING)
    private MealType type;

    public static MealPk create(LocalDate date, MealType type) {
        MealPk pk = new MealPk();
        pk.date = date;
        pk.type = type;
        return pk;
    }

}
