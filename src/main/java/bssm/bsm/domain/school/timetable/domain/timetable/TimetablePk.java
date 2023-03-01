package bssm.bsm.domain.school.timetable.domain.timetable;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
@NoArgsConstructor
public class TimetablePk implements Serializable {

    @Column
    private int grade;

    @Column(name = "class_no")
    private int classNo;

    public static TimetablePk create(int grade, int classNo) {
        TimetablePk pk = new TimetablePk();
        pk.grade = grade;
        pk.classNo = classNo;
        return pk;
    }
}
