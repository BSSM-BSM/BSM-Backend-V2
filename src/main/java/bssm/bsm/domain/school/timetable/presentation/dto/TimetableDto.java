package bssm.bsm.domain.school.timetable.presentation.dto;

import lombok.*;

import java.sql.Time;

@Getter
@Builder
@AllArgsConstructor
public class TimetableDto {

    private String className;
    private Time endTime;
    private Time startTime;
    private String type;

}
