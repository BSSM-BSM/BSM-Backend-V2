package bssm.bsm.domain.school.timetable.domain;

import bssm.bsm.domain.school.timetable.presentation.dto.TimetableDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.sql.Time;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimetableItem {

    @EmbeddedId
    private TimetableItemPk pk;

    @Column(length = 12)
    private String className;

    @Column(length = 8)
    private String type;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

    public TimetableDto toResponse() {
        return TimetableDto.builder()
                .className(className)
                .startTime(startTime)
                .endTime(endTime)
                .type(type)
                .build();
    }

}
