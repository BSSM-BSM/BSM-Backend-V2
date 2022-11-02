package bssm.bsm.domain.school.timetable.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateTimetableListRequest {

    @NotNull
    private List<TimetableItemRequest> timetableList;

}
