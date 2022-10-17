package bssm.bsm.domain.school.meister.service;

import bssm.bsm.domain.school.meister.domain.MeisterData;
import bssm.bsm.domain.school.meister.domain.MeisterInfo;
import bssm.bsm.domain.school.meister.facade.MeisterInfoFacade;
import bssm.bsm.domain.school.meister.domain.MeisterDataRepository;
import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.StudentRepository;
import bssm.bsm.domain.school.meister.presentation.dto.request.MeisterDetailRequest;
import bssm.bsm.domain.school.meister.presentation.dto.response.MeisterDetailResponse;
import bssm.bsm.domain.school.meister.presentation.dto.response.MeisterResponse;
import bssm.bsm.domain.school.meister.domain.MeisterInfoRepository;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MeisterService {

    private final StudentRepository studentRepository;
    private final MeisterInfoRepository meisterInfoRepository;
    private final MeisterDataRepository meisterDataRepository;
    private final MeisterParsingProvider meisterProvider;
    private final MeisterAuthProvider meisterAuthProvider;
    private final MeisterDataProvider meisterDataProvider;
    private final MeisterInfoFacade meisterInfoFacade;

    public MeisterDetailResponse getDetail(User user, MeisterDetailRequest dto) throws IOException {
        Student student = studentRepository.findByGradeAndClassNoAndStudentNo(dto.getGrade(), dto.getClassNo(), dto.getStudentNo()).orElseThrow(
                () -> {throw new NotFoundException("학생을 찾을 수 없습니다");}
        );
        if (!student.getStudentId().equals(user.getStudentId())) {
            meisterInfoFacade.getMeisterInfo(user.getStudentId()).permissionCheck();
        }

        MeisterData meisterData = meisterDataProvider.findOrElseCreateMeisterData(student);
        MeisterInfo meisterInfo = meisterData.getMeisterInfo();
        if (meisterInfo.isPrivateRanking() && !student.getStudentId().equals(user.getStudentId())) {
            throw new ForbiddenException("정보 공유를 거부한 유저입니다");
        }

        meisterAuthProvider.login(student, dto.getPw().isEmpty()? student.getStudentId(): dto.getPw());
        MeisterDetailResponse detailInfo = meisterProvider.getAllInfo(student);

        if (meisterInfo.isLoginError()) {
            meisterInfo.setLoginError(false);
            meisterInfoRepository.save(meisterInfo);
        }

        meisterData.setModifiedAt(LocalDateTime.now());
        meisterData.setScore(detailInfo.getScore());
        meisterData.setScoreRawData(detailInfo.getScoreHtmlContent());
        meisterData.setPositivePoint(detailInfo.getPositivePoint());
        meisterData.setNegativePoint(detailInfo.getNegativePoint());
        meisterData.setPointRawData(detailInfo.getPointHtmlContent());

        meisterDataRepository.save(meisterData);
        return detailInfo;
    }

    public MeisterResponse get(User user) {
        MeisterData meisterData = meisterDataRepository.findByStudentIdAndModifiedAtGreaterThan(user.getStudentId(), LocalDate.now().atStartOfDay()).orElseGet(
                () -> meisterDataProvider.getAndUpdateMeisterData(
                        meisterDataProvider.findOrElseCreateMeisterData(user.getStudent())
                )
        );
        MeisterInfo meisterInfo = meisterData.getMeisterInfo();

        if (meisterInfo.isLoginError()) {
            return MeisterResponse.builder()
                    .uniqNo(meisterInfo.getStudentId())
                    .lastUpdate(LocalDateTime.now())
                    .loginError(true)
                    .build();
        }

        return MeisterResponse.builder()
                .score(meisterData.getScore())
                .positivePoint(meisterData.getPositivePoint())
                .negativePoint(meisterData.getNegativePoint())
                .lastUpdate(meisterData.getModifiedAt())
                .loginError(false)
                .build();
    }

    public MeisterResponse updateAndGet(User user) {
        MeisterData meisterData = meisterDataProvider.getAndUpdateMeisterData(
                meisterDataProvider.findOrElseCreateMeisterData(user.getStudent())
        );
        MeisterInfo meisterInfo = meisterData.getMeisterInfo();

        if (meisterInfo.isLoginError()) {
            return MeisterResponse.builder()
                    .uniqNo(meisterInfo.getStudentId())
                    .lastUpdate(LocalDateTime.now())
                    .loginError(true)
                    .build();
        }

        return MeisterResponse.builder()
                .score(meisterData.getScore())
                .positivePoint(meisterData.getPositivePoint())
                .negativePoint(meisterData.getNegativePoint())
                .lastUpdate(meisterData.getModifiedAt())
                .loginError(false)
                .build();
    }

}
