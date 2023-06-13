package bssm.bsm.domain.board.lostfound.presentation;

import bssm.bsm.domain.board.lostfound.domain.type.Process;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundCompactRes;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundRes;
import bssm.bsm.domain.board.lostfound.service.LostFoundInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("lost-found/find")
@RequiredArgsConstructor
public class LostFoundInformationController {

    private final LostFoundInformationService lostFoundInformationService;

    @GetMapping("process/{process}")
    public List<LostFoundCompactRes> findAllByProcess(@PathVariable Process process) {
        return lostFoundInformationService.findByProcess(process);
    }

    @GetMapping("/{id}")
    public LostFoundRes findOne(@PathVariable Long id) {
        return lostFoundInformationService.findOne(id);
    }
}
