package bssm.bsm.domain.board.emoticon.repositories;

import bssm.bsm.domain.board.emoticon.entities.Emoticon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmoticonRepository extends JpaRepository<Emoticon, Long> {
}
