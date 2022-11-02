package bssm.bsm.domain.school.timetable.domain.timetable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class TimetableItemPk implements Serializable {

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Timetable timetable;

    @Column
    private int day;

    @Column
    private int idx;

    @Override
    public boolean equals(Object o) {
        return timetable.equals(((TimetableItemPk) o).timetable)
                && day == ((TimetableItemPk) o).day
                && idx == ((TimetableItemPk) o).idx;
    }

}
