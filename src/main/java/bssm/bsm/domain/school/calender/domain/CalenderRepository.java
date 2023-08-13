package bssm.bsm.domain.school.calender.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalenderRepository extends JpaRepository<Calender, Long> {

    @Query("select c from Calender c where c.classInfo.grade = :grade and c.classInfo.classNo = :classNo and c.date.month = :month order by c.date.day")
    List<Calender> findByClassInfoAndDate(@Param("grade") int grade, @Param("classNo") int classNo, @Param("month") int month);
}
