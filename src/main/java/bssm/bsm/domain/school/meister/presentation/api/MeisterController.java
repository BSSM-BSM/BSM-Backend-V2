package bssm.bsm.domain.school.meister.presentation.api;

import bssm.bsm.domain.school.meister.service.MeisterService;
import bssm.bsm.global.utils.UserUtil;
import bssm.bsm.domain.school.meister.presentation.dto.request.MeisterDetailRequest;
import bssm.bsm.domain.school.meister.presentation.dto.response.MeisterDetailResponse;
import bssm.bsm.domain.school.meister.presentation.dto.response.MeisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("meister")
@RequiredArgsConstructor
public class MeisterController {

    private final MeisterService meisterService;
    private final UserUtil userUtil;

    @PostMapping("detail")
    public MeisterDetailResponse getDetail(@RequestBody MeisterDetailRequest dto) throws IOException {
        return meisterService.getDetail(userUtil.getCurrentUser(), dto);
    }

    @GetMapping
    public MeisterResponse get() {
        return meisterService.get(userUtil.getCurrentUser());
    }

    @GetMapping("update")
    public MeisterResponse updateAndGet() {
        return meisterService.updateAndGet(userUtil.getCurrentUser());
    }

}
