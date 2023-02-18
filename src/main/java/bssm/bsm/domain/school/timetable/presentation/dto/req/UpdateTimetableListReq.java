package bssm.bsm.domain.school.timetable.presentation.dto.req;

import bssm.bsm.domain.school.timetable.presentation.dto.TimetableItemDto;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class UpdateTimetableListReq {

    private long id;

    @NotNull
    private List<TimetableItemDto> timetableList;

}
