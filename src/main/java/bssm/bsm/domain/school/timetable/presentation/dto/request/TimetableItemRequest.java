package bssm.bsm.domain.school.timetable.presentation.dto.request;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageItem;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageItemPk;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@NoArgsConstructor
public class TimetableItemRequest {

    private String className;
    private Time endTime;
    private Time startTime;
    private String type;
    private int day;
    private int idx;

    public TimetableManageItem toEntity(TimetableManage manage) {
        return TimetableManageItem.builder()
                .pk(
                        TimetableManageItemPk.builder()
                                .timetableManage(manage)
                                .day(day)
                                .idx(idx)
                                .build()
                )
                .className(className)
                .type(type)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

}
