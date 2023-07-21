package bssm.bsm.domain.board.lostfound.domain.repository;

import bssm.bsm.domain.board.lostfound.domain.type.State;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundCompactRes;

import java.util.List;

public interface CustomLostFoundRepository {
    List<LostFoundCompactRes> findAllByState(State state);
}
