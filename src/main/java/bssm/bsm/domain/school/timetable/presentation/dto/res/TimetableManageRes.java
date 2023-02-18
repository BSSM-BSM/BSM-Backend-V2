package bssm.bsm.domain.school.timetable.presentation.dto.res;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TimetableManageRes {

    private long id;
    private String name;
    private LocalDateTime modifiedAt;

    public static TimetableManageRes create(TimetableManage timetableManage) {
        TimetableManageRes res = new TimetableManageRes();
        res.id = timetableManage.getId();
        res.name = timetableManage.getName();
        res.modifiedAt = timetableManage.getModifiedAt();
        return res;
    }
}
