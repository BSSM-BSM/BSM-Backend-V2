package bssm.bsm.school.timetable;

import bssm.bsm.school.timetable.dto.request.TimetableRequestDto;
import bssm.bsm.school.timetable.entities.Timetable;
import bssm.bsm.school.timetable.repositories.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;

    public List<Timetable> getTimetable(TimetableRequestDto dto) {
        return timetableRepository.findByPkGradeAndPkClassNoAndPkDay(dto.getGrade(), dto.getClassNo(), dto.getDay());
    }
}
