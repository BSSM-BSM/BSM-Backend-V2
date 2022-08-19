package bssm.bsm.school.meister;

import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.school.meister.dto.request.GetMeisterPointDto;
import bssm.bsm.school.meister.dto.response.MeisterScoreAndPointResponseDto;
import bssm.bsm.user.entities.Student;
import bssm.bsm.user.repositories.StudentRepository;
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
    private final StudentRepository studentRepository;

    @PostMapping("scoreAndPoint")
    public MeisterScoreAndPointResponseDto getScoreAndPoint(@RequestBody GetMeisterPointDto dto) throws IOException {
        return meisterService.getScoreAndPoint(dto);
    }
}
