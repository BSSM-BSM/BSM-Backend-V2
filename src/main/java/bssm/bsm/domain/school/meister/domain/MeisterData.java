package bssm.bsm.domain.school.meister.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MeisterData {

    @Id
    @Column(name = "student_id", length = 10)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private MeisterInfo meisterInfo;

    @Setter
    @Column(nullable = false)
    @ColumnDefault("0")
    private float score;

    @Setter
    @Column(columnDefinition = "MEDIUMTEXT")
    private String scoreRawData;

    @Setter
    @Column(nullable = false)
    @ColumnDefault("0")
    private int positivePoint;

    @Setter
    @Column(nullable = false)
    @ColumnDefault("0")
    private int negativePoint;

    @Setter
    @Column(columnDefinition = "MEDIUMTEXT")
    private String pointRawData;

    @Setter
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

}
