package bssm.bsm.domain.school.timetable.domain.timetable;

import bssm.bsm.domain.school.timetable.presentation.dto.res.TimetableRes;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;

@Getter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimetableItem {

    @EqualsAndHashCode.Include
    @EmbeddedId
    private TimetableItemPk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "grade", insertable = false, updatable = false),
            @JoinColumn(name = "class_no", insertable = false, updatable = false)
    })
    private Timetable timetable;

    @Column(name = "day", insertable = false, updatable = false)
    private int day;

    @Column(name = "idx", insertable = false, updatable = false)
    private int idx;

    @Column(length = 30)
    private String className;

    @Column(length = 8)
    private String type;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

    public static TimetableItem create(Timetable timetable, int day, int idx,
                                       String className, String type, Time startTime, Time endTime) {
        TimetableItem item = new TimetableItem();
        item.pk = TimetableItemPk.create(timetable.getGrade(), timetable.getClassNo(), day, idx);
        item.className = className;
        item.type = type;
        item.startTime = startTime;
        item.endTime = endTime;
        return item;
    }

    public void update(TimetableItem item) {
        this.className = item.className;
        this.type = item.type;
        this.startTime = item.startTime;
        this.endTime = item.endTime;
    }

    public TimetableRes toResponse() {
        return TimetableRes.builder()
                .className(className)
                .startTime(startTime)
                .endTime(endTime)
                .type(type)
                .build();
    }

}
