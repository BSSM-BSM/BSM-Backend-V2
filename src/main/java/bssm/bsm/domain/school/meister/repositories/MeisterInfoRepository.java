package bssm.bsm.domain.school.meister.repositories;

import bssm.bsm.domain.school.meister.entities.MeisterInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeisterInfoRepository extends JpaRepository<MeisterInfo, String> {
    Optional<MeisterInfo> findByStudentIdAndModifiedAtGreaterThan(String studentId, LocalDateTime today);

    @EntityGraph(attributePaths = "student")
    List<MeisterInfo> findByOrderByScoreDesc();
}
