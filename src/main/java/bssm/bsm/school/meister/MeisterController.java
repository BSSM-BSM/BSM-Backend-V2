package bssm.bsm.school.meister;

import bssm.bsm.global.utils.UserUtil;
import bssm.bsm.school.meister.dto.request.MeisterDetailRequestDto;
import bssm.bsm.school.meister.dto.response.MeisterDetailResponseDto;
import bssm.bsm.school.meister.dto.response.MeisterResponseDto;
import bssm.bsm.user.repositories.StudentRepository;
import lombok.Getter;
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
    public MeisterDetailResponseDto getDetail(@RequestBody MeisterDetailRequestDto dto) throws IOException {
        return meisterService.getDetail(dto);
    }

    @GetMapping
    public MeisterResponseDto get() {
        return meisterService.get(userUtil.getCurrentUser());
    }
}
