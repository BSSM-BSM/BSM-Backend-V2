package bssm.bsm.domain.school.meister.entities;

import bssm.bsm.domain.user.entities.Student;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MeisterInfo {

    @Id
    @Column(length = 10)
    private String studentId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studentId", nullable = false, insertable = false, updatable = false)
    private Student student;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean loginError;

    @Column(nullable = false)
    private float score;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String scoreRawData;

    @Column(nullable = false)
    private int positivePoint;

    @Column(nullable = false)
    private int negativePoint;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String pointRawData;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean privateRanking;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public MeisterInfo(String studentId, Student student, boolean loginError, float score, String scoreRawData, int positivePoint, int negativePoint, String pointRawData, boolean privateRanking, LocalDateTime modifiedAt) {
        this.studentId = studentId;
        this.student = student;
        this.loginError = loginError;
        this.score = score;
        this.scoreRawData = scoreRawData;
        this.positivePoint = positivePoint;
        this.negativePoint = negativePoint;
        this.pointRawData = pointRawData;
        this.privateRanking = privateRanking;
        this.modifiedAt = modifiedAt;
    }

    public void setPrivateRanking(boolean privateRanking) {
        this.privateRanking = privateRanking;
    }
}