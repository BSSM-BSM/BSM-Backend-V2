package bssm.bsm.domain.user.presentation.dto.response;

import bssm.bsm.domain.user.domain.UserLevel;
import bssm.bsm.domain.user.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {

    private Long code;
    private String nickname;
    private String email;
    private int level;
    private UserRole role;
    private StudentInfoResponse student;
    private TeacherInfoResponse teacher;

}
