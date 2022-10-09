package bssm.bsm.domain.school.timetable.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.sql.Time;

@Getter
@Builder
public class TimetableResponse {

    private String className;
    private Time endTime;
    private Time startTime;
    private String type;
}
