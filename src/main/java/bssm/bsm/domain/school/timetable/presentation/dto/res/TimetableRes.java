package bssm.bsm.domain.school.timetable.presentation.dto.res;

import lombok.*;

import java.sql.Time;

@Getter
@Builder
@AllArgsConstructor
public class TimetableRes {

    private String className;
    private Time endTime;
    private Time startTime;
    private String type;

}
