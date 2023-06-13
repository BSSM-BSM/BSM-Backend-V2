package bssm.bsm.domain.board.lostfound.service;

import bssm.bsm.domain.board.lostfound.domain.LostFound;
import bssm.bsm.domain.board.lostfound.domain.repository.LostFoundRepository;
import bssm.bsm.domain.board.lostfound.domain.type.Process;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundCompactRes;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundRes;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostFoundInformationService {

    private final LostFoundRepository lostFoundRepository;

    public List<LostFoundCompactRes> findByProcess(Process process) {
        return lostFoundRepository.findAllByProcess(process);
    }

    public LostFoundRes findOne(Long id) {
        LostFound lostFound = lostFoundRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("분실물 게시글이 존재하지 않습니다."));

        return new LostFoundRes(lostFound);
    }
}
