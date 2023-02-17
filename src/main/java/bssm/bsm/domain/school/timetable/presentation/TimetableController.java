package bssm.bsm.domain.school.timetable.presentation;

import bssm.bsm.domain.school.timetable.presentation.dto.req.TimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableRes;
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

    @GetMapping("{grade}/{classNo}")
    public List<List<TimetableRes>> getTimetable(
            @PathVariable int grade,
            @PathVariable int classNo
    ) {
        return timetableService.getTimetableList(new TimetableReq(grade, classNo));
    }

}
