package bssm.bsm.domain.school.timetable.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Builder
public class TimetableRequestDto {

    private int grade;
    private int classNo;
    @Max(6) @Min(0)
    private int day;
}
