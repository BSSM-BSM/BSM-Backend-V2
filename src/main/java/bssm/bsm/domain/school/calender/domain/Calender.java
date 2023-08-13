package bssm.bsm.domain.school.calender.domain;

import bssm.bsm.domain.school.calender.presentation.dto.CalenderRes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Calender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Class classInfo;

    @Embedded
    private Date date;

    private String title;

    private String content;

    @Column(length = 8)
    private String color;

    @Builder
    public Calender(int classNo, int grade, int month, int day, String title, String content, String color) {
        this.classInfo = new Class(classNo, grade);
        this.title = title;
        this.date = new Date(month, day);
        this.content = content;
        this.color = color;
    }

    public void update(String title, String content, String color, int month, int day) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.date = new Date(month, day);
    }

    public CalenderRes toDto() {
        return new CalenderRes(
                id,
                date.getMonth(),
                date.getDay(),
                classInfo.getClassNo(),
                classInfo.getGrade(),
                title,
                content,
                color);
    }

    public int getDay() {
        return date.getDay();
    }
}
