package bssm.bsm.domain.board.lostfound.service;

import bssm.bsm.domain.board.lostfound.domain.LostFound;
import bssm.bsm.domain.board.lostfound.domain.repository.LostFoundRepository;
import bssm.bsm.domain.board.lostfound.domain.type.State;
import bssm.bsm.domain.board.lostfound.exception.NoSuchLostFoundException;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundCompactRes;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostFoundInformationService {

    private final LostFoundRepository lostFoundRepository;

    public List<LostFoundCompactRes> findByState(State state) {
        return lostFoundRepository.findAllByState(state);
    }

    public LostFoundRes findOne(Long id) {
        LostFound lostFound = lostFoundRepository.findById(id)
                .orElseThrow(NoSuchLostFoundException::new);

        return new LostFoundRes(lostFound);
    }
}
