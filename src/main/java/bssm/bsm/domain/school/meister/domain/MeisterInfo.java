package bssm.bsm.domain.school.meister.domain;

import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserRole;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeisterInfo {

    @Id
    @Column(name = "student_id", length = 10)
    private String studentId;

    @Setter
    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, insertable = false, updatable = false)
    private Student student;

    @Setter
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean loginError;

    @Setter
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean privateRanking;

    @Setter
    @Column(nullable = false)
    private LocalDateTime lastPrivateDate;

    @Builder
    public MeisterInfo(String studentId, Student student, boolean loginError, boolean privateRanking, LocalDateTime lastPrivateDate) {
        this.studentId = studentId;
        this.student = student;
        this.loginError = loginError;
        this.privateRanking = privateRanking;
        this.lastPrivateDate = lastPrivateDate;
    }

    public void privateCheck(User user) {
        if (user.getRole() == UserRole.TEACHER) return;

        if (privateRanking && !student.getId().equals(user.getStudent().getId())) {
            throw new ForbiddenException("정보 공유를 거부한 유저입니다");
        }
    }

}