package bssm.bsm.domain.school.timetable.domain.timetable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimetableRepository extends JpaRepository<Timetable, TimetablePk> {

    Optional<Timetable> findByPkGradeAndPkClassNo(int grade, int classNo);

}
