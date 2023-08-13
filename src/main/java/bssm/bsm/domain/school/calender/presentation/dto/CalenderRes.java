package bssm.bsm.domain.school.calender.presentation.dto;

import bssm.bsm.domain.school.calender.domain.Class;
import bssm.bsm.domain.school.calender.domain.Date;
import lombok.Getter;

@Getter
public class CalenderRes {
    private final Long id;
    private final Date date;
    private final Class classInfo;
    private final String title;
    private final String content;
    private final String color;

    public CalenderRes(Long id, int month, int day, int classNo, int grade, String title, String content, String color) {
        this.id = id;
        this.date = new Date(month, day);
        this.classInfo = new Class(classNo, grade);
        this.title = title;
        this.content = content;
        this.color = color;
    }
}
