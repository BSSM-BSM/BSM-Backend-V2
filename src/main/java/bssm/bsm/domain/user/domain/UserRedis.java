package bssm.bsm.domain.user.domain;

import bssm.bsm.global.entity.BaseTimeEntity;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "user")
public class UserRedis extends BaseTimeEntity {

    @Id
    @org.springframework.data.annotation.Id
    @Column(columnDefinition = "INT UNSIGNED")
    private Long code;

    @Column(nullable = false, length = 40, unique = true)
    private String nickname;

    @Column(nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(length = 10)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private Student student;

    @Column
    private Long teacherId;

    @OneToOne
    @JoinColumn(name = "teacherId", insertable = false, updatable = false)
    private Teacher teacher;

    @Column(nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private UserLevel level;

    @Column(nullable = false, length = 32)
    private String oauthToken;

    @Builder
    public UserRedis(Long code, String nickname, UserRole role, String studentId, Student student, Long teacherId, Teacher teacher, UserLevel level, String oauthToken) {
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
