package bssm.bsm.domain.school.timetable.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableItemRepository extends JpaRepository<TimetableItem, TimetableItemPk> {

    List<TimetableItem> findAllByPkGradeAndPkClassNoAndPkDay(int grade, int classNo, int day);

    List<TimetableItem> findAllByPkGradeAndPkClassNo(int grade, int classNo);

}
