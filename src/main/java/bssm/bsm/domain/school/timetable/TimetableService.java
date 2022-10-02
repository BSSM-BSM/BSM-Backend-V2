package bssm.bsm.domain.school.timetable;

import bssm.bsm.domain.school.timetable.dto.request.TimetableRequestDto;
import bssm.bsm.domain.school.timetable.entities.Timetable;
import bssm.bsm.domain.school.timetable.repositories.TimetableRepository;
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

    public List<Timetable> getTimetable(@Valid TimetableRequestDto dto) {
        return timetableRepository.findByPkGradeAndPkClassNoAndPkDay(dto.getGrade(), dto.getClassNo(), dto.getDay());
    }
}
