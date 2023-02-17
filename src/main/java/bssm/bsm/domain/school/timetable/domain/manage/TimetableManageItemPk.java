package bssm.bsm.domain.school.timetable.domain.manage;

import lombok.*;

import javax.persistence.*;
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
    private int day;

    @EqualsAndHashCode.Include
    @Column
    private int idx;

    public static TimetableManageItemPk create(TimetableManage timetableManage, int day, int idx) {
        TimetableManageItemPk pk = new TimetableManageItemPk();
        pk.timetableManageId = timetableManage.getId();
        pk.day = day;
        pk.idx = idx;
        return pk;
    }

}
