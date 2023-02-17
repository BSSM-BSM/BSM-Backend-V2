package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.manage.TimetableManage;
import bssm.bsm.domain.school.timetable.domain.manage.TimetableManageRepository;
import bssm.bsm.domain.school.timetable.exception.NoSuchTimetableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimetableManageProvider {

    private final TimetableManageRepository timetableManageRepository;

    public List<TimetableManage> findManageList(int grade, int classNo) {
        return timetableManageRepository
                .findAllByGradeAndClassNoOrderByModifiedAtDesc(grade, classNo);
    }

    public TimetableManage findManage(long id) {
        return timetableManageRepository.findById(id)
                .orElseThrow(NoSuchTimetableException::new);
    }

}
