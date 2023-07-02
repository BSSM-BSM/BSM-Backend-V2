package bssm.bsm.domain.school.timetable.domain.manage;

import bssm.bsm.domain.school.timetable.domain.TimetableType;
import bssm.bsm.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class TimetableManage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 12)
    private String name;

    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private TimetableType type;

    @Column
    private int grade;

    @Column
    private int classNo;

    @OneToMany(mappedBy = "timetableManage", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idx")
    private Set<TimetableManageItem> items = new HashSet<>();

    public static TimetableManage create(String name, TimetableType type, int grade, int classNo) {
        TimetableManage manage = new TimetableManage();
        manage.name = name;
        manage.type = type;
        manage.grade = grade;
        manage.classNo = classNo;
        return manage;
    }

    public void update(String name, TimetableType type) {
        this.name = name;
        this.type = type;
    }

    public void upsertItems(List<TimetableManageItem> items) {
        Map<TimetableManageItemPk, TimetableManageItem> map = new HashMap<>();
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
