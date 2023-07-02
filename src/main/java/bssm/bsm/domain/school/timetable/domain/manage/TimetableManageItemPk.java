package bssm.bsm.domain.school.timetable.domain.manage;

import bssm.bsm.domain.school.timetable.domain.TimetableDayType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
@NoArgsConstructor
public class TimetableManageItemPk implements Serializable {

    @EqualsAndHashCode.Include
    @Column
    private long timetableManageId;

    @EqualsAndHashCode.Include
    @Column
    private TimetableDayType day;

    @EqualsAndHashCode.Include
    @Column
    private int idx;

    public static TimetableManageItemPk create(TimetableManage timetableManage, TimetableDayType day, int idx) {
        TimetableManageItemPk pk = new TimetableManageItemPk();
        pk.timetableManageId = timetableManage.getId();
        pk.day = day;
        pk.idx = idx;
        return pk;
    }

}
