package bssm.bsm.domain.user.domain;

import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.domain.user.domain.type.UserRole;
import bssm.bsm.domain.user.exception.NoSuchUserEmailException;
import bssm.bsm.global.entity.BaseTimeEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public static User ofNormal(Long id, String nickname, String oauthToken) {
        User user = new User();
        user.id = id;
        user.nickname = nickname;
        user.level = UserLevel.USER;
        user.oauthToken = oauthToken;
        return user;
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public static User ofStudent(Student student, Long id, String nickname, String oauthToken) {
        User user = ofNormal(id, nickname, oauthToken);
        user.student = student;
        user.studentId = student.getStudentId();
        user.role = UserRole.STUDENT;
        return user;
    }

    public static User ofTeacher(Teacher teacher, Long id, String nickname, String oauthToken) {
        User user = ofNormal(id, nickname, oauthToken);
        user.teacher = teacher;
        user.teacherId = teacher.getTeacherId();
        user.role = UserRole.TEACHER;
        return user;
    }

    public static User ofCache(UserCache userCache) {
        User user = new User();
        user.id = userCache.getId();
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
