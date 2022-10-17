package bssm.bsm.domain.board.emoticon.domain;

import bssm.bsm.domain.board.emoticon.domain.Emoticon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmoticonRepository extends JpaRepository<Emoticon, Long> {

    boolean existsByName(String name);

    List<Emoticon> findAllByActiveAndDeleted(boolean active, boolean deleted);
}
