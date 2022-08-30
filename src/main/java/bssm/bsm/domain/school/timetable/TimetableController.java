package bssm.bsm.domain.school.timetable;

import bssm.bsm.domain.school.timetable.dto.request.TimetableRequestDto;
import bssm.bsm.domain.school.timetable.entities.Timetable;
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
    public List<Timetable> getTimetable(
            @PathVariable int grade,
            @PathVariable int classNo,
            @PathVariable int day
    ) {
        return timetableService.getTimetable(
                TimetableRequestDto.builder()
                        .grade(grade)
                        .classNo(classNo)
                        .day(day)
                        .build()
        );
    }
}
