package bssm.bsm.domain.school.timetable.presentation;

import bssm.bsm.domain.school.timetable.presentation.dto.TimetableDto;
import bssm.bsm.domain.school.timetable.presentation.dto.request.TimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableManageResponse;
import bssm.bsm.domain.school.timetable.service.TimetableManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin/timetable")
@RequiredArgsConstructor
public class TimetableManageController {

    private final TimetableManageService timetableManageService;

    @GetMapping("{grade}/{classNo}")
    public List<TimetableManageResponse> getDayTimetable(
            @PathVariable int grade,
            @PathVariable int classNo
    ) {
        return timetableManageService.getManageList(new TimetableRequest(grade, classNo));
    }

    @GetMapping("{id}")
    public List<List<TimetableDto>> getTimetable(@PathVariable long id) {
        return timetableManageService.getTimetableList(id);
    }

}
