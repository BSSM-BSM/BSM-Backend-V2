package bssm.bsm.domain.school.meister.facade;

import bssm.bsm.domain.school.meister.domain.MeisterInfo;
import bssm.bsm.domain.school.meister.domain.MeisterInfoRepository;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.UserRole;
import bssm.bsm.global.error.exceptions.ForbiddenException;
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

    public void viewPermissionCheck(User user) {
        if (user.getRole() == UserRole.TEACHER) return;

        MeisterInfo info = meisterInfoRepository.findById(user.getStudentId()).orElseThrow(
                () -> {throw new NotFoundException("마이스터 정보를 가져올 수 없습니다");}
        );
        if (info.isLoginError()) {
            throw new ForbiddenException("자신의 마이스터 정보를 불러올 수 있도록 설정해야 볼 수 있습니다\n마이스터 인증제 사이트에서 계정의 비밀번호를 초기 비밀번호로 설정해주세요");
        }
        if (info.isPrivateRanking()) {
            throw new ForbiddenException("자신의 랭킹 공유를 허용해야 볼 수 있습니다");
        }
    }

}
