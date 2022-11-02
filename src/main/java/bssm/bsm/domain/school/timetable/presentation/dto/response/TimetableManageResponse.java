package bssm.bsm.domain.school.timetable.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TimetableManageResponse {

    private long id;
    private String name;
    private LocalDateTime modifiedAt;

}
