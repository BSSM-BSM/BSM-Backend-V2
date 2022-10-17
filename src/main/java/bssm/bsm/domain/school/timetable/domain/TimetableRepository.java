package bssm.bsm.domain.school.timetable.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, TimetablePk> {

    List<Timetable> findAllByPkGradeAndPkClassNoAndPkDay(int grade, int classNo, int day);
}
