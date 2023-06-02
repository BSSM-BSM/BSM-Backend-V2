package bssm.bsm.domain.board.lostfound.domain.repository;

import bssm.bsm.domain.board.lostfound.domain.LostFound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostFoundRepository extends JpaRepository<LostFound, Long> {
}
