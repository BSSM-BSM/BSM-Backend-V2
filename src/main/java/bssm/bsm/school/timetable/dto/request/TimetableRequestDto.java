package bssm.bsm.school.timetable.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimetableRequestDto {

    private int grade;
    private int classNo;
    private int day;
}
