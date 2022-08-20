package bssm.bsm.school.meister.repositories;

import bssm.bsm.school.meister.entities.MeisterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MeisterInfoRepository extends JpaRepository<MeisterInfo, String> {
    Optional<MeisterInfo> findByUniqNoAndModifiedAtGreaterThan(String uniqNo, LocalDateTime today);
}
