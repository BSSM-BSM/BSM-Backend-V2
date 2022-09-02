package bssm.bsm.domain.school.meister.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeisterStudentResponseDto {

    private int grade;
    private int classNo;
    private int studentNo;
    private String name;
}
