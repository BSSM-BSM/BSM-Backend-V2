package bssm.bsm.domain.school.meister.entities;

import bssm.bsm.domain.user.entities.Student;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeisterInfo {

    @Id
    @Column(length = 10)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "studentId", nullable = false, insertable = false, updatable = false)
    private Student student;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean loginError;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean privateRanking;

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

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setPrivateRanking(boolean privateRanking) {
        this.privateRanking = privateRanking;
    }

    public void setLastPrivateDate(LocalDateTime lastPrivateDate) {
        this.lastPrivateDate = lastPrivateDate;
    }

    public void setLoginError(boolean loginError) {
        this.loginError = loginError;
    }
}