package bssm.bsm.domain.school.meister.presentation.dto.request;


import lombok.Getter;

@Getter
public class MeisterDetailRequest {

    private int grade;
    private int classNo;
    private int studentNo;
    private String pw;
}
