package bssm.bsm.domain.school.timetable.domain.manage;

import bssm.bsm.domain.school.timetable.domain.timetable.Timetable;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableItem;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableItemPk;
import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.sql.Time;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimetableManageItem {

    @EmbeddedId
    private TimetableManageItemPk pk;

    @Column(length = 12)
    private String className;

    @Column(length = 8)
    private String type;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

    @Builder
    public TimetableManageItem(TimetableManageItemPk pk, String className, String type, Time startTime, Time endTime) {
        this.pk = pk;
        this.className = className;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimetableResponse toResponse() {
        return TimetableResponse.builder()
                .className(className)
                .startTime(startTime)
                .endTime(endTime)
                .type(type)
                .build();
    }

    public TimetableItem toTimetableItem(Timetable timetable) {
        return TimetableItem.builder()
                .pk(
                        TimetableItemPk.builder()
                                .timetable(timetable)
                                .day(pk.getDay())
                                .idx(pk.getIdx())
                                .build()
                )
                .className(className)
                .type(type)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        return pk.equals(((TimetableManageItem)o).pk);
    }

}
