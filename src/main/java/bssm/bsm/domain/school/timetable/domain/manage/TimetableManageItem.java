package bssm.bsm.domain.school.timetable.domain.manage;

import bssm.bsm.domain.school.timetable.domain.TimetableDayType;
import bssm.bsm.domain.school.timetable.domain.timetable.Timetable;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableItem;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.sql.Time;

@Getter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimetableManageItem {

    @EqualsAndHashCode.Include
    @EmbeddedId
    private TimetableManageItemPk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_manage_id")
    @MapsId("timetableManageId")
    private TimetableManage timetableManage;

    @Column(name = "day", insertable = false, updatable = false)
    private TimetableDayType day;

    @Column(name = "idx", insertable = false, updatable = false)
    private int idx;

    @Column(length = 30)
    private String className;

    @Column(length = 8)
    private String type;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

    public static TimetableManageItem create(TimetableManage timetableManage, TimetableDayType day, int idx,
                                             String className, String type, Time startTime, Time endTime) {
        TimetableManageItem item = new TimetableManageItem();
        item.pk = TimetableManageItemPk.create(timetableManage, day, idx);
        item.timetableManage = timetableManage;
        item.className = className;
        item.type = type;
        item.startTime = startTime;
        item.endTime = endTime;
        return item;
    }

    public void update(TimetableManageItem item) {
        this.className = item.className;
        this.type = item.type;
        this.startTime = item.startTime;
        this.endTime = item.endTime;
    }

    public TimetableItem toTimetableItem(Timetable timetable) {
        return TimetableItem.create(
                timetable,
                day,
                idx,
                className,
                type,
                startTime,
                endTime);
    }

}
