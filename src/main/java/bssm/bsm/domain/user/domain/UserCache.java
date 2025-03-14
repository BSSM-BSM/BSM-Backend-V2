package bssm.bsm.domain.user.domain;

import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.domain.user.domain.type.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "user")
public class UserCache {

    @Id
    private Long id;
    private String nickname;
    private UserRole role;
    private Student student;
    private Teacher teacher;
    private UserLevel level;
    private String oauthToken;

    public static UserCache ofUser(User user) {
        UserCache userCache = new UserCache();
        userCache.id = user.getId();
        userCache.nickname = user.getNickname();
        userCache.role = user.getRole();
        userCache.student = user.getStudent();
        userCache.teacher = user.getTeacher();
        userCache.level = user.getLevel();
        userCache.oauthToken = user.getOauthToken();
        return userCache;
    }

}
