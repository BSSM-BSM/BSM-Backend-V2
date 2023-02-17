package bssm.bsm.domain.school.timetable.presentation;

import bssm.bsm.domain.school.timetable.presentation.dto.req.UpdateTimetableListReq;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableRes;
import bssm.bsm.domain.school.timetable.presentation.dto.req.CreateTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.TimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.UpdateTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableManageRes;
import bssm.bsm.domain.school.timetable.service.TimetableManageService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public List<TimetableManageRes> getDayTimetable(@PathVariable int grade, @PathVariable int classNo) {
        return timetableManageService.getManageList(new TimetableReq(grade, classNo));
    }

    @PostMapping
    public void createTimetable(@Valid @RequestBody CreateTimetableReq dto) {
        timetableManageService.createTimetable(dto);
    }

    @PutMapping("{id}")
    public void updateTimetable(
            @PathVariable long id,
            @Valid @RequestBody UpdateTimetableReq dto
    ) {
        timetableManageService.updateTimetable(id, dto);
    }

    @PutMapping("{id}/list")
    public void updateTimetableList(
            @PathVariable long id,
            @Valid @RequestBody UpdateTimetableListReq dto
    ) {
        timetableManageService.updateTimetableList(id, dto);
    }

    @PutMapping("{id}/apply")
    public void applyTimetable(@PathVariable long id) throws JsonProcessingException {
        timetableManageService.applyTimetable(id);
    }

    @DeleteMapping("{id}")
    public void deleteTimetable(@PathVariable long id) {
        timetableManageService.deleteTimetable(id);
    }

    @GetMapping("{id}")
    public List<List<TimetableRes>> getTimetable(@PathVariable long id) {
        return timetableManageService.getTimetableList(id);
    }

}
