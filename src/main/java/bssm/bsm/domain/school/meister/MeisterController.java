package bssm.bsm.domain.school.meister;

import bssm.bsm.global.utils.UserUtil;
import bssm.bsm.domain.school.meister.dto.request.MeisterDetailRequestDto;
import bssm.bsm.domain.school.meister.dto.response.MeisterDetailResponseDto;
import bssm.bsm.domain.school.meister.dto.response.MeisterRankingDto;
import bssm.bsm.domain.school.meister.dto.response.MeisterResponseDto;
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
    public MeisterDetailResponseDto getDetail(@RequestBody MeisterDetailRequestDto dto) throws IOException {
        return meisterService.getDetail(dto);
    }

    @GetMapping
    public MeisterResponseDto get() {
        return meisterService.get(userUtil.getCurrentUser());
    }

    @GetMapping("update")
    public MeisterResponseDto updateAndGet() {
        return meisterService.updateAndGet(userUtil.getCurrentUser());
    }

    @GetMapping("ranking")
    public List<MeisterRankingDto> getRanking() {
        return meisterService.getRanking();
    }
}
