package bssm.bsm.domain.school.meister.presentation;

import bssm.bsm.domain.school.meister.service.MeisterRankingService;
import bssm.bsm.domain.school.meister.presentation.dto.request.UpdateMeisterPrivateRequest;
import bssm.bsm.domain.school.meister.presentation.dto.response.MeisterRankingResponse;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("meister")
@RequiredArgsConstructor
public class MeisterRankingController {

    private final MeisterRankingService meisterRankingService;
    private final CurrentUser currentUser;

    @GetMapping("ranking/{grade}")
    public List<MeisterRankingResponse> getRanking(@PathVariable int grade) {
        return meisterRankingService.getRanking(currentUser.getUser(), grade);
    }

    @PutMapping("privateRanking")
    public void updatePrivateRanking(@RequestBody UpdateMeisterPrivateRequest dto) {
        meisterRankingService.updatePrivateRanking(currentUser.getUser(), dto.isPrivateRanking());
    }

}
