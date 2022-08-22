package bssm.bsm.school.timetable.repositories;

import bssm.bsm.school.timetable.entities.Timetable;
import bssm.bsm.school.timetable.entities.TimetablePk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, TimetablePk> {

    List<Timetable> findByPkGradeAndPkClassNoAndPkDay(int grade, int classNo, int day);
}
