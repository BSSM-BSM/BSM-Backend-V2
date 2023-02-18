package bssm.bsm.domain.school.timetable.presentation.dto.res;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageItem;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableItem;
import lombok.*;

import java.sql.Time;

@Getter
@NoArgsConstructor
public class TimetableItemRes {

    private String className;
    private Time endTime;
    private Time startTime;
    private String type;

    public static TimetableItemRes create(TimetableItem timetableItem) {
        TimetableItemRes res = new TimetableItemRes();
        res.className = timetableItem.getClassName();
        res.endTime = timetableItem.getEndTime();
        res.startTime = timetableItem.getStartTime();
        res.type = timetableItem.getType();
        return res;
    }

    public static TimetableItemRes create(TimetableManageItem manageItem) {
        TimetableItemRes res = new TimetableItemRes();
        res.className = manageItem.getClassName();
        res.endTime = manageItem.getEndTime();
        res.startTime = manageItem.getStartTime();
        res.type = manageItem.getType();
        return res;
    }
}
