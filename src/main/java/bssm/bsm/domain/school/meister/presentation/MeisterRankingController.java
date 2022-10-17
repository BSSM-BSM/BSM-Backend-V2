package bssm.bsm.domain.school.meister.presentation;

import bssm.bsm.domain.school.meister.service.MeisterRankingService;
import bssm.bsm.domain.school.meister.presentation.dto.request.UpdateMeisterPrivateRequest;
import bssm.bsm.domain.school.meister.presentation.dto.response.MeisterRankingResponse;
import bssm.bsm.global.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("meister")
@RequiredArgsConstructor
public class MeisterRankingController {

    private final MeisterRankingService meisterRankingService;
    private final UserUtil userUtil;

    @GetMapping("ranking")
    public List<MeisterRankingResponse> getRanking() {
        return meisterRankingService.getRanking(userUtil.getUser());
    }

    @PutMapping("privateRanking")
    public void updatePrivateRanking(@RequestBody UpdateMeisterPrivateRequest dto) {
        meisterRankingService.updatePrivateRanking(userUtil.getUser(), dto.isPrivateRanking());
    }

}
