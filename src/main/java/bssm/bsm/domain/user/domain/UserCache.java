package bssm.bsm.domain.user.domain;

import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.domain.user.domain.type.UserRole;
import bssm.bsm.global.entity.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "user")
public class UserCache extends BaseTimeEntity {

    @Id
    private Long code;
    private String nickname;
    private UserRole role;
    private String studentId;
    private Student student;
    private Long teacherId;
    private Teacher teacher;
    private UserLevel level;
    private String oauthToken;

    public static UserCache ofUser(User user) {
        UserCache userCache = new UserCache();
        userCache.code = user.getCode();
        userCache.nickname = user.getNickname();
        userCache.role = user.getRole();
        userCache.studentId = user.getStudentId();
        userCache.student = user.getStudent();
        userCache.teacherId = user.getTeacherId();
        userCache.teacher = user.getTeacher();
        userCache.level = user.getLevel();
        userCache.oauthToken = user.getOauthToken();
        return userCache;
    }

}
