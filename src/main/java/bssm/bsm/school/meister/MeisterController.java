package bssm.bsm.school.meister;

import bssm.bsm.school.meister.dto.request.FindStudentInfoDto;
import bssm.bsm.school.meister.dto.request.GetMeisterPointDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("meister")
@RequiredArgsConstructor
public class MeisterController {

    private final MeisterService meisterService;

    @PostMapping("score")
    public String getScore(@RequestBody FindStudentInfoDto dto) throws IOException {
        return meisterService.getScore(dto);
    }

    @PostMapping("point")
    public String getPoint(@RequestBody GetMeisterPointDto dto) throws IOException {
        return meisterService.getPoint(dto);
    }
}
