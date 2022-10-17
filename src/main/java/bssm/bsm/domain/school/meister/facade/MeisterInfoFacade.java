package bssm.bsm.domain.school.meister.facade;

import bssm.bsm.domain.school.meister.domain.MeisterInfo;
import bssm.bsm.domain.school.meister.domain.MeisterInfoRepository;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeisterInfoFacade {

    private final MeisterInfoRepository meisterInfoRepository;

    public MeisterInfo getMeisterInfo(String studentId) {
        return meisterInfoRepository.findById(studentId).orElseThrow(
                () -> {throw new NotFoundException("마이스터 정보를 가져올 수 없습니다");}
        );
    }

}
