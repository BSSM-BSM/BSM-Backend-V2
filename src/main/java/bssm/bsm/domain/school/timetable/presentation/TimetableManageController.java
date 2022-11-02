package bssm.bsm.domain.school.timetable.presentation;

import bssm.bsm.domain.school.timetable.presentation.dto.request.UpdateTimetableListRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableResponse;
import bssm.bsm.domain.school.timetable.presentation.dto.request.CreateTimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.request.TimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.request.UpdateTimetableRequest;
import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableManageResponse;
import bssm.bsm.domain.school.timetable.service.TimetableManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping
    public void createTimetable(@Valid @RequestBody CreateTimetableRequest dto) {
        timetableManageService.createTimetable(dto);
    }

    @PutMapping("{id}")
    public void updateTimetable(
            @PathVariable long id,
            @Valid @RequestBody UpdateTimetableRequest dto
    ) {
        timetableManageService.updateTimetable(id, dto);
    }

    @PutMapping("{id}/list")
    public void updateTimetableList(
            @PathVariable long id,
            @Valid @RequestBody UpdateTimetableListRequest dto
    ) {
        timetableManageService.updateTimetableList(id, dto);
    }

    @GetMapping("{id}")
    public List<List<TimetableResponse>> getTimetable(@PathVariable long id) {
        return timetableManageService.getTimetableList(id);
    }

}
