package bssm.bsm.domain.school.timetable.presentation.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class FindTimetableReq {

    @Positive
    private int grade;

    @Positive
    private int classNo;

}
