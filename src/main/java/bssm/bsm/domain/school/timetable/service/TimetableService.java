package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.TimetableItem;
import bssm.bsm.domain.school.timetable.presentation.dto.request.DayTimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.request.TimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.TimetableDto;
import bssm.bsm.domain.school.timetable.domain.TimetableItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableItemRepository timetableRepository;

    public List<TimetableDto> getDayTimetable(@Valid DayTimetableRequest dto) {
        return timetableRepository.findAllByPkGradeAndPkClassNoAndPkDay(dto.getGrade(), dto.getClassNo(), dto.getDay())
                .stream().map(TimetableItem::toResponse).toList();
    }

    public List<List<TimetableDto>> getTimetable(@Valid TimetableRequest dto) {
        Map<Integer, List<TimetableDto>> timetableMap = new HashMap<>();
        timetableRepository.findAllByPkGradeAndPkClassNo(dto.getGrade(), dto.getClassNo()).forEach(timetable -> {
            int day = timetable.getPk().getDay();
            if (timetableMap.get(day) == null) {
                timetableMap.put(day, new ArrayList<>(List.of(timetable.toResponse())));
                return;
            }
            timetableMap.get(day).add(timetable.toResponse());
        });

        return new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6))
                .stream().map(timetableMap::get)
                .toList();
    }

}
