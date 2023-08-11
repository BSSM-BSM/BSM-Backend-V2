package bssm.bsm.domain.school.timetable.presentation;

import bssm.bsm.domain.school.timetable.presentation.dto.req.FindTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableListRes;
import bssm.bsm.domain.school.timetable.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("timetable")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;

    @GetMapping("{grade}/{classNo}")
    public TimetableListRes findTimetable(
            @PathVariable int grade,
            @PathVariable int classNo
    ) {
        return timetableService.findTimetableList(new FindTimetableReq(grade, classNo));
    }

}
