package bssm.bsm.domain.user.domain;

import bssm.bsm.global.entity.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "user")
public class UserRedis extends BaseTimeEntity {

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

    @Builder
    public UserRedis(Long code, String nickname, UserRole role, String studentId, Student student, Long teacherId, Teacher teacher, UserLevel level, String oauthToken, int point) {
        this.code = code;
        this.nickname = nickname;
        this.role = role;
        this.studentId = studentId;
        this.student = student;
        this.teacherId = teacherId;
        this.teacher = teacher;
        this.level = level;
        this.oauthToken = oauthToken;
    }

    public User toUser() {
        return User.builder()
                .code(code)
                .nickname(nickname)
                .role(role)
                .studentId(studentId)
                .student(student)
                .teacherId(teacherId)
                .teacher(teacher)
                .level(level)
                .oauthToken(oauthToken)
                .build();
    }

}
