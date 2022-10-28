package bssm.bsm.domain.school.meal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class MealPk implements Serializable {

    @Column
    private LocalDate date;

    @Column(nullable = false, length = 7)
    @Enumerated(EnumType.STRING)
    private MealType type;

}
