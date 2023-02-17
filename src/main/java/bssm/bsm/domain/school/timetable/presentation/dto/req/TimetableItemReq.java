package bssm.bsm.domain.school.timetable.presentation.dto.req;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageItem;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Time;

@Getter
public class TimetableItemReq {

    @NotBlank
    private String className;

    @NotNull
    private Time endTime;

    @NotNull
    private Time startTime;

    @NotNull
    private String type;

    private int day;

    private int idx;

    public TimetableManageItem toEntity(TimetableManage timetableManage) {
        return TimetableManageItem.create(timetableManage, day, idx, className, type, startTime, endTime);
    }

}
