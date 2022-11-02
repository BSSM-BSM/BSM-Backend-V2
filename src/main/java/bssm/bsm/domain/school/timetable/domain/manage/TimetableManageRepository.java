package bssm.bsm.domain.school.timetable.domain.manage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimetableManageRepository extends JpaRepository<TimetableManage, Long> {

    List<TimetableManage> findAllByGradeAndClassNoOrderByModifiedAtDesc(int grade, int classNo);

}
