package bssm.bsm.domain.school.meister;

import bssm.bsm.domain.school.meister.dto.request.UpdateMeisterPrivateRequest;
import bssm.bsm.global.utils.UserUtil;
import bssm.bsm.domain.school.meister.dto.request.MeisterDetailRequest;
import bssm.bsm.domain.school.meister.dto.response.MeisterDetailResponse;
import bssm.bsm.domain.school.meister.dto.response.MeisterRankingResponse;
import bssm.bsm.domain.school.meister.dto.response.MeisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("ranking")
    public List<MeisterRankingResponse> getRanking() {
        return meisterService.getRanking(userUtil.getCurrentUser());
    }

    @PutMapping("privateRanking")
    public void updatePrivateRanking(@RequestBody UpdateMeisterPrivateRequest dto) {
        meisterService.updatePrivateRanking(userUtil.getCurrentUser(), dto.isPrivateRanking());
    }
}
