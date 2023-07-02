package bssm.bsm.domain.school.meister.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MeisterData {

    @Id
    @Column(length = 10)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "meisterId", nullable = false)
    private MeisterInfo meisterInfo;

    @Column(nullable = false)
    @ColumnDefault("0")
    private float score;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String scoreRawData;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int positivePoint;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int negativePoint;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String pointRawData;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public MeisterData(String studentId, MeisterInfo meisterInfo, float score, String scoreRawData, int positivePoint, int negativePoint, String pointRawData) {
        this.studentId = studentId;
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

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

}
