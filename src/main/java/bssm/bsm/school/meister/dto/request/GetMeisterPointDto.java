package bssm.bsm.school.meister.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class GetMeisterPointDto {

    private int grade;
    private int classNo;
    private int studentNo;
    private String pw;
}
