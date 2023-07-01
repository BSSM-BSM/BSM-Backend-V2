package bssm.bsm.domain.user.domain;

import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.domain.user.domain.type.UserRole;
import bssm.bsm.domain.user.exception.NoSuchUserEmailException;
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

    public String findEmailOrNull() {
        if (this.role == UserRole.STUDENT) {
            return this.student.getEmail();
        }
        if (this.role == UserRole.TEACHER) {
            return this.teacher.getEmail();
        }
        throw new NoSuchUserEmailException();
    }

    public static User ofNormal(Long code, String nickname, String oauthToken) {
        User user = new User();
        user.code = code;
        user.nickname = nickname;
        user.level = UserLevel.USER;
        user.oauthToken = oauthToken;
        return user;
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public static User ofStudent(Student student, Long code, String nickname, String oauthToken) {
        User user = ofNormal(code, nickname, oauthToken);
        user.student = student;
        user.studentId = student.getStudentId();
        user.role = UserRole.STUDENT;
        return user;
    }

    public static User ofTeacher(Teacher teacher, Long code, String nickname, String oauthToken) {
        User user = ofNormal(code, nickname, oauthToken);
        user.teacher = teacher;
        user.teacherId = teacher.getTeacherId();
        user.role = UserRole.TEACHER;
        return user;
    }

    public static User ofCache(UserCache userCache) {
        User user = new User();
        user.code = userCache.getCode();
        user.nickname = userCache.getNickname();
        user.role = userCache.getRole();
        user.studentId = userCache.getStudentId();
        user.student = userCache.getStudent();
        user.teacherId = userCache.getTeacherId();
        user.teacher = userCache.getTeacher();
        user.level = userCache.getLevel();
        user.oauthToken = userCache.getOauthToken();
        return user;
    }

}
