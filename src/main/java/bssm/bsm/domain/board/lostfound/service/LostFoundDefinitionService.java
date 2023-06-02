package bssm.bsm.domain.board.lostfound.service;

import bssm.bsm.domain.board.lostfound.domain.type.Process;
import bssm.bsm.domain.board.lostfound.presentation.dto.req.LostFoundReq;
import bssm.bsm.domain.board.lostfound.presentation.dto.req.UpdateProcessReq;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundRes;
import bssm.bsm.domain.board.lostfound.domain.LostFound;
import bssm.bsm.domain.board.lostfound.domain.repository.LostFoundRepository;
import bssm.bsm.global.auth.CurrentUser;
import bssm.bsm.global.error.exceptions.NotFoundException;
import bssm.bsm.global.error.exceptions.UnAuthorizedException;
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
                .user(currentUser.getUser())
                .createdLocalDateTime(LocalDateTime.now())
                .build();

        lostFoundRepository.save(newLostFound);

        return new LostFoundRes(newLostFound);
    }

    @Transactional
    public LostFoundRes updateProcess(Long lostFoundId, UpdateProcessReq updateProcessReq) {
        LostFound lostFound = lostFoundRepository.findById(lostFoundId)
                .orElseThrow(() -> new NotFoundException("cannot find lostFound"));

        if (!Objects.equals(currentUser.getUser().getCode(), lostFound.getFoundUser().getCode())) {
            throw new UnAuthorizedException("you are not user create this");
        }

        lostFound.updateProcess(updateProcessReq.getProcess());

        return new LostFoundRes(lostFound);
    }
}
