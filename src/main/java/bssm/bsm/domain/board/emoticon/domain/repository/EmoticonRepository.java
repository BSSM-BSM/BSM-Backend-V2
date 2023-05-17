package bssm.bsm.domain.board.emoticon.domain.repository;

import bssm.bsm.domain.board.emoticon.domain.Emoticon;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmoticonRepository extends JpaRepository<Emoticon, Long> {

    boolean existsByName(String name);

    @EntityGraph(attributePaths = "items")
    List<Emoticon> findAllByActiveAndDeletedOrderByTotalViewDesc(boolean active, boolean deleted);
}
