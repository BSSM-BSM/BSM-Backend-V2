package bssm.bsm.domain.school.timetable.domain.timetable;

import bssm.bsm.domain.school.timetable.domain.TimetableType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timetable {

    @EmbeddedId
    private TimetablePk pk;

    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private TimetableType type;

    @OneToMany(mappedBy = "pk.timetable", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private final List<TimetableItem> items = new ArrayList<>();

    @Builder
    public Timetable(TimetablePk pk, TimetableType type) {
        this.pk = pk;
        this.type = type;
    }

}
