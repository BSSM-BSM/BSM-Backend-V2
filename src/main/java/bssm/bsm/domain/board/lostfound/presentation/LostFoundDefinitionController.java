package bssm.bsm.domain.board.lostfound.presentation;

import bssm.bsm.domain.board.lostfound.presentation.dto.req.LostFoundReq;
import bssm.bsm.domain.board.lostfound.presentation.dto.req.UpdateProcessReq;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundRes;
import bssm.bsm.domain.board.lostfound.service.LostFoundDefinitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("lost-found/def")
@RequiredArgsConstructor
public class LostFoundDefinitionController {
    private final LostFoundDefinitionService lostFoundDefinitionService;

    @PostMapping
    public LostFoundRes create(@Valid @RequestBody LostFoundReq lostFoundReq) {
        return lostFoundDefinitionService.create(lostFoundReq);
    }

    @PutMapping("/update/{id}")
    public LostFoundRes updateProcess(@PathVariable Long id, @Valid @RequestBody UpdateProcessReq updateProcessReq) {
        return lostFoundDefinitionService.updateProcess(id, updateProcessReq);
    }
}
