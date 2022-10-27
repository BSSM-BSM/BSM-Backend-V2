package bssm.bsm.domain.school.timetable.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class TimetableItemPk implements Serializable {

    @Column
    private int grade;

    @Column
    private int classNo;

    @Column
    private int day;

    @Column
    private int idx;
}
