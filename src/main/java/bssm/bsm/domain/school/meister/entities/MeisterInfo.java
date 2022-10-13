package bssm.bsm.domain.school.meister.entities;

import bssm.bsm.domain.user.entities.Student;
import bssm.bsm.domain.user.entities.User;
import bssm.bsm.global.error.exceptions.ForbiddenException;
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

    public void permissionCheck() {
        if (loginError) {
            throw new ForbiddenException("자신의 마이스터 정보를 불러올 수 있도록 설정해야 볼 수 있습니다\n마이스터 인증제 사이트에서 계정의 비밀번호를 초기 비밀번호로 설정해주세요");
        }
        if (privateRanking) {
            throw new ForbiddenException("자신의 랭킹 공유를 허용해야 볼 수 있습니다");
        }
    }

}