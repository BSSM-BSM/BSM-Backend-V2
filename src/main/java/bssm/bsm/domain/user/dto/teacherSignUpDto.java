package bssm.bsm.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class teacherSignUpDto {

    private Long userCode;
    private String nickname;
    private String email;
    private String name;
}
