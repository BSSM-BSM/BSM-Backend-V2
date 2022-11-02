package bssm.bsm.domain.school.timetable.presentation.dto.request;

import bssm.bsm.domain.school.timetable.domain.TimetableType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CreateTimetableRequest {

    @NotBlank
    @Size(max = 12)
    private String name;

    @Column
    private TimetableType type;

    @Column
    private int grade;

    @Column
    private int classNo;

}
