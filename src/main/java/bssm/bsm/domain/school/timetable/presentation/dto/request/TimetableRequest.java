package bssm.bsm.domain.school.timetable.presentation.dto.request;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Positive;

@Getter
public class TimetableRequest {

    @Positive
    private int grade;

    @Positive
    private int classNo;

    public TimetableRequest(int grade, int classNo) {
        this.grade = grade;
        this.classNo = classNo;
    }

}
