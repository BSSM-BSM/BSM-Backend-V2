package bssm.bsm.domain.school.meister.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeisterStudentResponse {

    private int grade;
    private int classNo;
    private int studentNo;
    private String name;
}
