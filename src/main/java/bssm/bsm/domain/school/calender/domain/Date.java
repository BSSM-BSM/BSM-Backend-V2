package bssm.bsm.domain.school.calender.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Date {
    @Column(columnDefinition = "INT(1) UNSIGNED")
    private int month;

    @Column(columnDefinition = "INT(2) UNSIGNED")
    private int day;
}
