package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.presentation.dto.request.TimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableResponse;
import bssm.bsm.domain.school.timetable.domain.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;

    public List<TimetableResponse> getTimetable(@Valid TimetableRequest dto) {
        return timetableRepository.findAllByPkGradeAndPkClassNoAndPkDay(dto.getGrade(), dto.getClassNo(), dto.getDay())
                .stream().map(timetable -> TimetableResponse.builder()
                        .className(timetable.getClassName())
                        .startTime(timetable.getStartTime())
                        .endTime(timetable.getEndTime())
                        .type(timetable.getType())
                        .build()
                ).toList();
    }
}
