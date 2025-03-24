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
    private Long id;

    @Column(nullable = false, length = 40)
    private String nickname;

    @Column(nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "teacher_id")
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
        user.role = UserRole.STUDENT;
        return user;
    }

    public static User ofTeacher(Teacher teacher, Long id, String nickname, String oauthToken) {
        User user = ofNormal(id, nickname, oauthToken);
        user.teacher = teacher;
        user.role = UserRole.TEACHER;
        return user;
    }

    public static User ofCache(UserCache userCache) {
        User user = new User();
        user.id = userCache.getId();
        user.nickname = userCache.getNickname();
        user.role = userCache.getRole();
        user.student = userCache.getStudent();
        user.teacher = userCache.getTeacher();
        user.level = userCache.getLevel();
        user.oauthToken = userCache.getOauthToken();
        return user;
    }

}
