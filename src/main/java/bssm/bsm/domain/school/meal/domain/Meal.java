package bssm.bsm.domain.school.meal.domain;

import bssm.bsm.global.error.exceptions.NotFoundException;
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

    public String getMorningForFacade() {
        if (this.morning == null) throw new NotFoundException();
        return morning;
    }

    public String getLunchForFacade() {
        if (this.lunch == null) throw new NotFoundException();
        return lunch;
    }

    public String getDinnerForFacade() {
        if (this.dinner == null) throw new NotFoundException();
        return dinner;
    }

}
