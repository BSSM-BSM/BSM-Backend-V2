package bssm.bsm.domain.school.meal.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meal {

    @Id
    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String morning;

    @Column(columnDefinition = "TEXT")
    private String lunch;

    @Column(columnDefinition = "TEXT")
    private String dinner;

    public Meal(LocalDate date) {
        this.date = date;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }
}
