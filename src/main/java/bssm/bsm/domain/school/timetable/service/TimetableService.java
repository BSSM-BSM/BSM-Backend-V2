package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.timetable.Timetable;
import bssm.bsm.domain.school.timetable.presentation.dto.req.TimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableProvider timetableProvider;

    public List<List<TimetableRes>> getTimetableList(@Valid TimetableReq dto) {
        Timetable timetable = timetableProvider.findTimetable(dto.getGrade(), dto.getClassNo());

        Map<Integer, List<TimetableRes>> timetableMap = new HashMap<>();
        timetable.getItems().forEach(item -> {
            int day = item.getPk().getDay();
            if (timetableMap.get(day) == null) {
                timetableMap.put(day, new ArrayList<>(List.of(item.toResponse())));
                return;
            }
            timetableMap.get(day).add(item.toResponse());
        });

        return new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6))
                .stream().map(timetableMap::get)
                .toList();
    }

}
