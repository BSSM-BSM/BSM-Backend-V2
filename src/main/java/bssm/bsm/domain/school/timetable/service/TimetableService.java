package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.timetable.Timetable;
import bssm.bsm.domain.school.timetable.presentation.dto.req.FindTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableProvider timetableProvider;

    public TimetableListRes findTimetableList(@Valid FindTimetableReq req) {
        Timetable timetable = timetableProvider.findTimetable(req.getGrade(), req.getClassNo());
        return TimetableListRes.create(timetable);
    }

}
