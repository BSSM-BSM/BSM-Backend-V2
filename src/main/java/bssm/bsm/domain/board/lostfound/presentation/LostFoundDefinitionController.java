package bssm.bsm.domain.board.lostfound.presentation;

import bssm.bsm.domain.board.lostfound.presentation.dto.req.LostFoundReq;
import bssm.bsm.domain.board.lostfound.presentation.dto.req.UpdateProcessReq;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundRes;
import bssm.bsm.domain.board.lostfound.service.LostFoundDefinitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("lost-found/def")
@RequiredArgsConstructor
public class LostFoundDefinitionController {
    private final LostFoundDefinitionService lostFoundDefinitionService;

    @PostMapping
    public LostFoundRes create(@Valid @RequestBody LostFoundReq lostFoundReq) {
        return lostFoundDefinitionService.create(lostFoundReq);
    }

    @DeleteMapping("/{id}")
    public LostFoundRes updateProcess(@PathVariable Long id) {
        return lostFoundDefinitionService.updateProcess(id);
    }
}
