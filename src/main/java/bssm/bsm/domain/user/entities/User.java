package bssm.bsm.domain.user.entities;

import bssm.bsm.domain.user.type.UserLevel;
import bssm.bsm.domain.user.type.UserRole;
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
