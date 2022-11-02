package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import bssm.bsm.domain.school.timetable.domain.timetable.Timetable;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableRepository;
import bssm.bsm.domain.school.timetable.presentation.dto.request.TimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableResponse;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableItemRepository;
import bssm.bsm.global.error.exceptions.NotFoundException;
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

    private final TimetableRepository timetableRepository;

    public List<List<TimetableResponse>> getTimetableList(@Valid TimetableRequest dto) {
        Timetable timetable = timetableRepository.findByPkGradeAndPkClassNo(dto.getGrade(), dto.getClassNo()).orElseThrow(NotFoundException::new);

        Map<Integer, List<TimetableResponse>> timetableMap = new HashMap<>();
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
