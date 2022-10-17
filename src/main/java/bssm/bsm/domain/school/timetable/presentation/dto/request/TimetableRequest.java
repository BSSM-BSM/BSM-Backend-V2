package bssm.bsm.domain.school.timetable.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Getter
@Builder
public class TimetableRequest {

    @Positive
    private int grade;

    @Positive
    private int classNo;

    @Max(6) @Min(0)
    private int day;
}
