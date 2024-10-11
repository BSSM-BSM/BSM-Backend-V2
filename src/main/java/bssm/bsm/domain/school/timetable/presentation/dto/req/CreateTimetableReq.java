package bssm.bsm.domain.school.timetable.presentation.dto.req;

import bssm.bsm.domain.school.timetable.domain.TimetableType;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
public class CreateTimetableReq {

    @NotBlank
    @Size(max = 12)
    private String name;

    @NotNull
    private TimetableType type;

    private int grade;

    private int classNo;

}
