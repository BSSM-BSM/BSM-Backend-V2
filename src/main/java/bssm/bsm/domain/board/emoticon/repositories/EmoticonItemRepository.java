package bssm.bsm.domain.board.emoticon.repositories;

import bssm.bsm.domain.board.emoticon.entities.EmoticonItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmoticonItemRepository extends JpaRepository<EmoticonItem, Long> {
}
