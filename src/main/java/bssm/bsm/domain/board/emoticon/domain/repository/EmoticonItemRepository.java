package bssm.bsm.domain.board.emoticon.domain.repository;

import bssm.bsm.domain.board.emoticon.domain.EmoticonItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmoticonItemRepository extends JpaRepository<EmoticonItem, Long> {
}
