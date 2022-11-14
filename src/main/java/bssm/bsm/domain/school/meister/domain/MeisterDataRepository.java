package bssm.bsm.domain.school.meister.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeisterDataRepository extends JpaRepository<MeisterData, String> {

    Optional<MeisterData> findByStudentIdAndModifiedAtGreaterThan(String studentId, LocalDateTime today);

    @EntityGraph(attributePaths = {"meisterInfo", "meisterInfo.student"})
    List<MeisterData> findByMeisterInfoStudentGradeOrderByScoreDesc(int grade);
}
