package bssm.bsm.school.timetable.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Time;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Timetable {

    @JsonIgnore
    @EmbeddedId
    private TimetablePk pk;

    @Column(length = 12)
    private String className;

    @Column(length = 8)
    private String type;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;
}
