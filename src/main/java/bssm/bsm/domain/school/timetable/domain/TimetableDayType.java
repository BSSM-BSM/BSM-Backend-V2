package bssm.bsm.domain.school.timetable.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TimetableDayType {

    SUN(0),
    MON(1),
    TUE(2),
    WED(3),
    THU(4),
    FRI(5),
    SAT(6);

    private final int value;
}
