package bssm.bsm.domain.user.presentation.dto.res;

import bssm.bsm.domain.user.domain.Teacher;
import bssm.bsm.domain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeacherRes {

    private String name;

    public static TeacherRes ofTeacher(Teacher teacher) {
        TeacherRes res = new TeacherRes();
        res.name = teacher.getName();
        return res;
    }

    public static TeacherRes ofUser(User user) {
        if (user.getTeacher() == null) {
            return null;
        }
        return ofTeacher(user.getTeacher());
    }

}
