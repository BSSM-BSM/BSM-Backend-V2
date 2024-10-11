package bssm.bsm.domain.school.timetable.domain.timetable;

import bssm.bsm.domain.school.timetable.domain.TimetableDayType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
@NoArgsConstructor
public class TimetableItemPk implements Serializable {

    @EqualsAndHashCode.Include
    @Column
    private int grade;

    @EqualsAndHashCode.Include
    @Column(name = "class_no")
    private int classNo;

    @EqualsAndHashCode.Include
    @Column
    private TimetableDayType day;

    @EqualsAndHashCode.Include
    @Column
    private int idx;

    public static TimetableItemPk create(int grade, int classNo, TimetableDayType day, int idx) {
        TimetableItemPk pk = new TimetableItemPk();
        pk.grade = grade;
        pk.classNo = classNo;
        pk.day = day;
        pk.idx = idx;
        return pk;
    }

}
