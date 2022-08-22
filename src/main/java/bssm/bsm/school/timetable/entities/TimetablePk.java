package bssm.bsm.school.timetable.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class TimetablePk implements Serializable {

    @Column
    private int grade;

    @Column
    private int classNo;

    @Column
    private int day;

    @Column
    private int idx;
}
