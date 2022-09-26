package bssm.bsm.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class studentDto {

    private long userCode;
    private String nickname;
    private int enrolledAt;
    private int grade;
    private int classNo;
    private int studentNo;
}
