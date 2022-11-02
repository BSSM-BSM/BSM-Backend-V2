package bssm.bsm.domain.school.timetable.domain.manage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class TimetableManageItemPk implements Serializable {

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TimetableManage timetableManage;

    @Column
    private int day;

    @Column
    private int idx;

    @Override
    public boolean equals(Object o) {
        return timetableManage.equals(((TimetableManageItemPk) o).timetableManage)
                && day == ((TimetableManageItemPk) o).day
                && idx == ((TimetableManageItemPk) o).idx;
    }

}
