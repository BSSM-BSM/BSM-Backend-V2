package bssm.bsm.domain.school.timetable.service;

import bssm.bsm.domain.school.timetable.domain.TimetableManageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimetableManageProvider {

    private final TimetableManageRepository timetableManageRepository;

}
