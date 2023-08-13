package bssm.bsm.domain.school.calender.service;

import bssm.bsm.domain.school.calender.domain.Calender;
import bssm.bsm.domain.school.calender.domain.CalenderRepository;
import bssm.bsm.domain.school.calender.domain.Class;
import bssm.bsm.domain.school.calender.domain.Date;
import bssm.bsm.domain.school.calender.presentation.dto.CalenderReq;
import bssm.bsm.domain.school.calender.presentation.dto.CalenderRes;
import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.repository.StudentRepository;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@RequiredArgsConstructor
public class CalenderService {
    private final StudentRepository studentRepository;
    private final CalenderRepository calenderRepository;

    @Transactional
    public void create(CalenderReq calenderReq, User user) {
        isSameClass(new Class(calenderReq.getClassNo(), calenderReq.getGrade()), user);

        calenderRepository.save(
                Calender.builder()
                        .classNo(calenderReq.getClassNo())
                        .grade(calenderReq.getGrade())
                        .month(calenderReq.getMonth())
                        .day(calenderReq.getDay())
                        .title(calenderReq.getTitle())
                        .content(calenderReq.getContent())
                        .color(calenderReq.getColor())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public List<List<CalenderRes>> read(Class classInfo, int month) {
        List<Calender> byClassInfoAndDate = calenderRepository.findByClassInfoAndDate(classInfo.getGrade(), classInfo.getClassNo(), month);

        return groupByDay(byClassInfoAndDate);
    }

    @Transactional
    public void update(CalenderReq calenderReq, User user, Long calenderId) {
        isSameClass(new Class(calenderReq.getClassNo(), calenderReq.getGrade()), user);

        Calender calender = calenderRepository.findById(calenderId)
                .orElseThrow(() -> new NotFoundException("Can't Found Updatable Calender"));

        calender.update(calenderReq.getTitle(), calenderReq.getContent(), calenderReq.getColor(), calenderReq.getMonth(), calenderReq.getDay());
    }

    @Transactional
    public void delete(Long calenderId, User user) {
        Calender calender = calenderRepository.findById(calenderId)
                .orElseThrow(() -> new NotFoundException("Calender Not Found"));

        isSameClass(calender.getClassInfo(), user);

        calenderRepository.delete(calender);
    }

    private void isSameClass(Class classInfo, User user) {
        Student userStudent = studentRepository.findById(user.getStudentId())
                .orElseThrow(() -> new NotFoundException("User Not Found Validating Class"));

        if (classInfo.getGrade() != userStudent.getGrade() || classInfo.getClassNo() != userStudent.getClassNo()) {
            throw new ForbiddenException("Can't Fix Another Class");
        }
    }

    public static List<List<CalenderRes>> groupByDay(List<Calender> calendarList) {
        Map<Integer, List<CalenderRes>> dayToCalendarsMap = new HashMap<>();

        // Group calendars by day
        for (Calender calendar : calendarList) {
            int day = calendar.getDay();
            dayToCalendarsMap.putIfAbsent(day, new ArrayList<>());
            dayToCalendarsMap.get(day).add(calendar.toDto());
        }

        // Create a list of lists with calendars grouped by day
        List<List<CalenderRes>> result = new ArrayList<>();
        for (int day = 1; day <= 31; day++) {
            result.add(dayToCalendarsMap.getOrDefault(day, new ArrayList<>()));
        }

        return result;
    }
}
