package bssm.bsm.domain.school.timetable.presentation.dto.res;

import bssm.bsm.domain.school.timetable.domain.TimetableDayType;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import bssm.bsm.domain.school.timetable.domain.timetable.Timetable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class TimetableListRes {

    private final Map<TimetableDayType, List<TimetableItemRes>> timetableList = new HashMap<>();

    public static TimetableListRes create(Timetable timetable) {
        TimetableListRes res = new TimetableListRes();
        timetable.getItems().forEach(item -> {
            TimetableDayType day = item.getPk().getDay();
            List<TimetableItemRes> itemList = res.timetableList.get(day);
            if (itemList == null) {
                itemList = new ArrayList<>(List.of(TimetableItemRes.create(item)));
                res.timetableList.put(day, itemList);
                return;
            }
            itemList.add(TimetableItemRes.create(item));
        });
        return res;
    }

    public static TimetableListRes create(TimetableManage timetableManage) {
        TimetableListRes res = new TimetableListRes();
        timetableManage.getItems().forEach(item -> {
            TimetableDayType day = item.getPk().getDay();
            List<TimetableItemRes> itemList = res.timetableList.get(day);
            if (itemList == null) {
                itemList = new ArrayList<>(List.of(TimetableItemRes.create(item)));
                res.timetableList.put(day, itemList);
                return;
            }
            itemList.add(TimetableItemRes.create(item));
        });
        return res;
    }
}
