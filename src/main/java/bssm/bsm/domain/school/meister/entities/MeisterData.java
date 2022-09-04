package bssm.bsm.domain.school.meister.entities;

import bssm.bsm.domain.user.entities.Student;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MeisterData {

    @Id
    @Column(length = 10, nullable = false)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "studentId", nullable = false, insertable = false, updatable = false)
    private Student student;

    @Column(length = 10, nullable = false)
    private String meisterId;

    @OneToOne
    @JoinColumn(name = "meisterId", nullable = false, insertable = false, updatable = false)
    private MeisterInfo meisterInfo;

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

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public MeisterData(String studentId, Student student, String meisterId, MeisterInfo meisterInfo, float score, String scoreRawData, int positivePoint, int negativePoint, String pointRawData) {
        this.studentId = studentId;
        this.student = student;
        this.meisterId = meisterId;
        this.meisterInfo = meisterInfo;
        this.score = score;
        this.scoreRawData = scoreRawData;
        this.positivePoint = positivePoint;
        this.negativePoint = negativePoint;
        this.pointRawData = pointRawData;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setScoreRawData(String scoreRawData) {
        this.scoreRawData = scoreRawData;
    }

    public void setPositivePoint(int positivePoint) {
        this.positivePoint = positivePoint;
    }

    public void setNegativePoint(int negativePoint) {
        this.negativePoint = negativePoint;
    }

    public void setPointRawData(String pointRawData) {
        this.pointRawData = pointRawData;
    }
}
