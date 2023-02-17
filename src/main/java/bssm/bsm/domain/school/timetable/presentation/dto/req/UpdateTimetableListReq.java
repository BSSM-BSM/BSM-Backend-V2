package bssm.bsm.domain.school.timetable.presentation.dto.req;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class UpdateTimetableListReq {

    @NotNull
    private List<TimetableItemReq> timetableList;

}
