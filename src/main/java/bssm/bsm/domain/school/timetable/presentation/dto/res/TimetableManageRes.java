package bssm.bsm.domain.school.timetable.presentation.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TimetableManageRes {

    private long id;
    private String name;
    private LocalDateTime modifiedAt;

}
