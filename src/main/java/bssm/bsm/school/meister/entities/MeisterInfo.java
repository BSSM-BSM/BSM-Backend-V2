package bssm.bsm.school.meister.entities;

import bssm.bsm.user.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class MeisterInfo {

    @Id
    @Column(length = 10)
    private String uniqNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uniqNo", insertable = false, updatable = false)
    private Student student;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean loginError;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private int positivePoint;

    @Column(nullable = false)
    private int negativePoint;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
