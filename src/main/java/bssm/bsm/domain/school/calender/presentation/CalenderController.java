package bssm.bsm.domain.school.calender.presentation;

import bssm.bsm.domain.school.calender.domain.Class;
import bssm.bsm.domain.school.calender.presentation.dto.CalenderReq;
import bssm.bsm.domain.school.calender.presentation.dto.CalenderRes;
import bssm.bsm.domain.school.calender.service.CalenderService;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("calender")
@RequiredArgsConstructor
public class CalenderController {

    private final CurrentUser currentUser;
    private final CalenderService calenderService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CalenderReq calenderReq) {
        calenderService.create(calenderReq, currentUser.getUser());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/read/{grade}/{classNumber}/{month}")
    public ResponseEntity<List<List<CalenderRes>>> read(@PathVariable int grade, @PathVariable int classNumber, @PathVariable int month) {
        List<List<CalenderRes>> read = calenderService.read(new Class(classNumber, grade), month);

        return ResponseEntity.ok().body(read);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestBody CalenderReq calenderReq, @PathVariable Long id) {
        calenderService.update(calenderReq, currentUser.getUser(), id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{calenderId}")
    public ResponseEntity<Object> delete(@PathVariable Long calenderId) {
        calenderService.delete(calenderId, currentUser.getUser());

        return ResponseEntity.ok().build();
    }
}
