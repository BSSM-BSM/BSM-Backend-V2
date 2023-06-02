package bssm.bsm.domain.board.lostfound.presentation;

import bssm.bsm.domain.board.lostfound.presentation.dto.req.LostFoundReq;
import bssm.bsm.domain.board.lostfound.presentation.dto.req.UpdateProcessReq;
import bssm.bsm.domain.board.lostfound.presentation.dto.res.LostFoundRes;
import bssm.bsm.domain.board.lostfound.service.LostFoundCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("lostfound")
@RequiredArgsConstructor
public class LostFoundController {
    private final LostFoundCreateService lostFoundCreateService;

    @PostMapping
    public LostFoundRes create(@RequestBody LostFoundReq lostFoundReq) {
        return lostFoundCreateService.create(lostFoundReq);
    }

    @PutMapping("/update/{id}")
    public LostFoundRes updateProcess(@PathVariable Long id, @RequestBody UpdateProcessReq updateProcessReq) {
        return lostFoundCreateService.updateProcess(id, updateProcessReq);
    }
}