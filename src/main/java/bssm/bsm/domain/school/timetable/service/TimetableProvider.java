package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.timetable.Timetable;
import bssm.bsm.domain.school.timetable.domain.timetable.TimetableRepository;
import bssm.bsm.domain.school.timetable.exception.NoSuchTimetableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimetableProvider {

    private final TimetableRepository timetableRepository;

    public Timetable findTimetable(int grade, int classNo) {
        return timetableRepository.findByPkGradeAndPkClassNo(grade, classNo)
                .orElseThrow(NoSuchTimetableException::new);
    }

}
