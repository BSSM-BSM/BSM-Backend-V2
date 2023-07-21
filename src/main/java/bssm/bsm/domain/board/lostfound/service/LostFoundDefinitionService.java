package bssm.bsm.domain.board.lostfound.service;

import bssm.bsm.domain.board.lostfound.domain.LostFound;
import bssm.bsm.domain.board.lostfound.domain.repository.LostFoundRepository;
import bssm.bsm.domain.board.lostfound.domain.type.Process;
import bssm.bsm.domain.board.lostfound.exception.NoSuchLostFoundException;
import bssm.bsm.domain.board.lostfound.exception.NotCreatorException;
import bssm.bsm.domain.board.lostfound.presentation.dto.req.LostFoundReq;
import bssm.bsm.domain.board.lostfound.presentation.dto.req.UpdateProcessReq;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundRes;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LostFoundDefinitionService {

    private final LostFoundRepository lostFoundRepository;
    private final CurrentUser currentUser;

    @Transactional
    public LostFoundRes create(LostFoundReq lostFoundReq) {

        LostFound newLostFound = LostFound.builder()
                .objectName(lostFoundReq.getObjectName())
                .imgSrc(lostFoundReq.getImgSrc())
                .description(lostFoundReq.getDescription())
                .location(lostFoundReq.getLocation())
                .findDateTime(lostFoundReq.getFindDateTime())
                .process(Process.IN_PROGRESS)
                .state(lostFoundReq.getState())
                .user(currentUser.getUser())
                .createdLocalDateTime(LocalDateTime.now())
                .build();

        lostFoundRepository.save(newLostFound);

        return new LostFoundRes(newLostFound);
    }

    @Transactional
    public LostFoundRes updateProcess(Long lostFoundId) {
        LostFound lostFound = lostFoundRepository.findById(lostFoundId)
                .orElseThrow(NoSuchLostFoundException::new);

        if (!Objects.equals(currentUser.getUser().getCode(), lostFound.getFoundUser().getCode())) {
            throw new NotCreatorException();
        }

        lostFound.updateProcess(Process.FINISHED);

        return new LostFoundRes(lostFound);
    }
}
