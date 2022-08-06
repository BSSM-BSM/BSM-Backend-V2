package bssm.bsm.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignUpDto {

    private int usercode;
    private String nickname;
    private int enrolledAt;
    private int grade;
    private int classNo;
    private int studentNo;
}
