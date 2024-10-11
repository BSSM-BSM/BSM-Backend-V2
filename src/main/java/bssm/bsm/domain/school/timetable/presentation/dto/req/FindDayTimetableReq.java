package bssm.bsm.domain.school.timetable.presentation.dto.req;

import lombok.Getter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Getter
public class FindDayTimetableReq extends FindTimetableReq {

    @Max(6) @Min(0)
    private int day;

    public FindDayTimetableReq(int grade, int classNo, int day) {
        super(grade, classNo);
        this.day = day;
    }

}
