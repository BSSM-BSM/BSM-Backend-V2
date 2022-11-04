package bssm.bsm.domain.school.timetable.presentation.dto.request;

import bssm.bsm.domain.school.timetable.domain.TimetableType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CreateTimetableRequest {

    @NotBlank
    @Size(max = 12)
    private String name;

    @NotNull
    private TimetableType type;

    private int grade;

    private int classNo;

}
