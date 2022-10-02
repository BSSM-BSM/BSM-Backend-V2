package bssm.bsm.domain.school.meister.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MeisterDetailRequestDto {

    private int grade;
    private int classNo;
    private int studentNo;
    private String pw;
}
