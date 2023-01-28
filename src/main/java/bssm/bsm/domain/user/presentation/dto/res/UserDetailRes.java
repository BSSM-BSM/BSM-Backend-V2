package bssm.bsm.domain.user.presentation.dto.res;

import bssm.bsm.domain.user.domain.type.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailRes {

    private Long code;
    private String nickname;
    private String email;
    private int level;
    private UserRole role;
    private StudentRes student;
    private TeacherRes teacher;

}
