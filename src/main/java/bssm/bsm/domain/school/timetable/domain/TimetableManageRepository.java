package bssm.bsm.domain.school.timetable.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimetableManageRepository extends JpaRepository<TimetableManage, Long> {

    List<TimetableManage> findAllByGradeAndClassNo(int grade, int classNo);

}
