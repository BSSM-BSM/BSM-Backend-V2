package bssm.bsm.domain.school.timetable.presentation;

import bssm.bsm.domain.school.timetable.presentation.dto.request.DayTimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.request.TimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.TimetableDto;
import bssm.bsm.domain.school.timetable.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("timetable")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;

    @GetMapping("{grade}/{classNo}/{day}")
    public List<TimetableDto> getDayTimetable(
            @PathVariable int grade,
            @PathVariable int classNo,
            @PathVariable int day
    ) {
        return timetableService.getDayTimetable(new DayTimetableRequest(grade, classNo, day));
    }

    @GetMapping("{grade}/{classNo}")
    public List<List<TimetableDto>> getTimetable(
            @PathVariable int grade,
            @PathVariable int classNo
    ) {
        return timetableService.getTimetable(new TimetableRequest(grade, classNo));
    }

}
