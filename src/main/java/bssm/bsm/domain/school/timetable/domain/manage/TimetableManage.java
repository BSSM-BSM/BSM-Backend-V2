package bssm.bsm.domain.school.timetable.domain.manage;

import bssm.bsm.domain.school.meal.domain.MealType;
import bssm.bsm.domain.school.timetable.domain.TimetableType;
import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableManageResponse;
import bssm.bsm.global.entity.BaseTimeEntity;
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

    @OneToMany(mappedBy = "pk.timetableManage", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private final List<TimetableManageItem> items = new ArrayList<>();

    @Builder
    public TimetableManage(long id, String name, TimetableType type, int grade, int classNo) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.grade = grade;
        this.classNo = classNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TimetableType type) {
        this.type = type;
    }

    public TimetableManageResponse toResponse() {
        return new TimetableManageResponse(id, name, getModifiedAt());
    }

}
