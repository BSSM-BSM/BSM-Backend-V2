package bssm.bsm.domain.school.timetable.presentation;

import bssm.bsm.domain.school.timetable.presentation.dto.req.ApplyTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.CreateTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.FindTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.UpdateTimetableListReq;
import bssm.bsm.domain.school.timetable.presentation.dto.req.UpdateTimetableReq;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableListRes;
import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableManageRes;
import bssm.bsm.domain.school.timetable.service.TimetableManageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin/timetable")
@RequiredArgsConstructor
public class TimetableManageController {

    private final TimetableManageService timetableManageService;

    @GetMapping("{grade}/{classNo}")
    public List<TimetableManageRes> findManageList(@PathVariable int grade, @PathVariable int classNo) {
        return timetableManageService.findManageList(new FindTimetableReq(grade, classNo));
    }

    @PostMapping
    public void createTimetable(@Valid @RequestBody CreateTimetableReq req) {
        timetableManageService.createTimetable(req);
    }

    @PutMapping
    public void updateTimetable(@Valid @RequestBody UpdateTimetableReq req) {
        timetableManageService.updateTimetable(req);
    }

    @PutMapping("list")
    public void updateTimetableList(@Valid @RequestBody UpdateTimetableListReq req) {
        timetableManageService.updateTimetableList(req);
    }

    @PutMapping("apply")
    public void applyTimetable(@RequestBody ApplyTimetableReq req) throws JsonProcessingException {
        timetableManageService.applyTimetable(req);
    }

    @DeleteMapping("{id}")
    public void deleteTimetable(@PathVariable long id) {
        timetableManageService.deleteTimetable(id);
    }

    @GetMapping("{id}")
    public TimetableListRes getTimetable(@PathVariable long id) {
        return timetableManageService.getTimetableList(id);
    }

}
