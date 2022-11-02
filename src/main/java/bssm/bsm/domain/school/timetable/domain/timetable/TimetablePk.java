package bssm.bsm.domain.school.timetable.domain.timetable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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

}
