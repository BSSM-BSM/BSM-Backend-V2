package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.TimetableManage;
import bssm.bsm.domain.school.timetable.domain.TimetableManageRepository;
import bssm.bsm.domain.school.timetable.presentation.dto.TimetableDto;
import bssm.bsm.domain.school.timetable.presentation.dto.request.TimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableManageResponse;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
@RequiredArgsConstructor
public class TimetableManageService {

    private final TimetableManageRepository timetableManageRepository;

    public List<TimetableManageResponse> getManageList(@Valid TimetableRequest dto) {
        return timetableManageRepository.findAllByGradeAndClassNo(dto.getGrade(), dto.getClassNo())
                .stream().map(TimetableManage::toResponse).toList();
    }

    public List<List<TimetableDto>> getTimetableList(@Valid @Positive Long id) {
        TimetableManage timetable = timetableManageRepository.findById(id).orElseThrow(NotFoundException::new);

        Map<Integer, List<TimetableDto>> timetableMap = new HashMap<>();
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
