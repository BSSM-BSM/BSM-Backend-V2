package bssm.bsm.domain.user.domain;

import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.domain.user.domain.type.UserRole;
import bssm.bsm.domain.user.presentation.dto.res.UserDetailRes;
import bssm.bsm.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
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
    public User(Long code, String nickname, UserRole role, String studentId, Student student, Long teacherId, Teacher teacher, UserLevel level, String oauthToken) {
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

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public UserDetailRes toUserInfoResponse() {
        UserDetailRes.UserDetailResBuilder builder = UserDetailRes.builder()
                .code(code)
                .role(role)
                .level(level.getValue())
                .nickname(nickname);

        return (
            switch (role) {
                case STUDENT -> builder
                        .email(student.getEmail())
                        .student(student.toInfo());
                case TEACHER -> builder
                        .email(teacher.getEmail())
                        .teacher(teacher.toInfo());
            }
        ).build();
    }

    public UserCache toUserCache() {
        return UserCache.builder()
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
