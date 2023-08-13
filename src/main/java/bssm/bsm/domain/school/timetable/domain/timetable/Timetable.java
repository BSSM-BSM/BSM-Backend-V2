package bssm.bsm.domain.school.timetable.domain.timetable;

import bssm.bsm.domain.school.timetable.domain.TimetableType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timetable {

    @EmbeddedId
    private TimetablePk pk;

    @Column(name = "grade", insertable = false, updatable = false)
    private int grade;

    @Column(name = "class_no", insertable = false, updatable = false)
    private int classNo;

    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private TimetableType type;

    @OneToMany(mappedBy = "timetable", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idx")
    private final Set<TimetableItem> items = new HashSet<>();

    public static Timetable create(int grade, int classNo, TimetableType type) {
        Timetable timetable = new Timetable();
        timetable.pk = TimetablePk.create(grade, classNo);
        timetable.type = type;
        return timetable;
    }

    public void upsertItems(List<TimetableItem> items) {
        Map<TimetableItemPk, TimetableItem> map = new HashMap<>();
        items.forEach(item -> map.put(item.getPk(), item));
        // 삭제하려는 기존의 아이템들만 remove
        this.items.removeIf(item -> !items.contains(item));
        // 기존의 아이템들 update
        this.items.forEach(item ->
                item.update(map.get(item.getPk()))
        );
        // 새로 추가된 아이템들 add
        this.items.addAll(items.stream()
                .filter(item -> !this.items.contains(item))
                .toList());
    }

}
