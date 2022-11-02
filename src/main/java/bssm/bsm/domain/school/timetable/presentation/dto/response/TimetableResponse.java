package bssm.bsm.domain.school.timetable.presentation.dto.response;

import lombok.*;

import java.sql.Time;

@Getter
@Builder
@AllArgsConstructor
public class TimetableResponse {

    private String className;
    private Time endTime;
    private Time startTime;
    private String type;

}
