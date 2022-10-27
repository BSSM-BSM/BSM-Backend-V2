package bssm.bsm.domain.school.timetable.domain;

import bssm.bsm.domain.school.timetable.presentation.dto.response.TimetableManageResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimetableManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 12)
    private String name;

    @Column
    private int grade;

    @Column
    private int classNo;

    @OneToMany(mappedBy = "pk.timetableManage", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private final List<TimetableManageItem> items = new ArrayList<>();

    public TimetableManageResponse toResponse() {
        return new TimetableManageResponse(id, name);
    }

}
