package bssm.bsm.domain.user.presentation.dto.res;

import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class UserDetailRes {

    private Long code;
    private String nickname;
    private String email;
    private int level;
    private UserRole role;
    private StudentRes student;
    private TeacherRes teacher;

    public static UserDetailRes create(User user) {
        UserDetailRes res = new UserDetailRes();
        res.code = user.getCode();
        res.nickname = user.getNickname();
        res.email = user.findEmailOrNull();
        res.level = user.getLevel().getValue();
        res.role = user.getRole();
        res.student = StudentRes.ofUser(user);
        res.teacher = TeacherRes.ofUser(user);
        return res;
    }

}
