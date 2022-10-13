package bssm.bsm.domain.school.meister.service;

import bssm.bsm.domain.school.meister.entities.MeisterData;
import bssm.bsm.domain.school.meister.entities.MeisterInfo;
import bssm.bsm.domain.school.meister.presentation.dto.response.MeisterDetailResponse;
import bssm.bsm.domain.school.meister.repositories.MeisterDataRepository;
import bssm.bsm.domain.school.meister.repositories.MeisterInfoRepository;
import bssm.bsm.domain.user.entities.Student;
import bssm.bsm.domain.user.repositories.StudentRepository;
import bssm.bsm.global.error.HttpError;
import bssm.bsm.global.error.exceptions.BadRequestException;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import bssm.bsm.global.error.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MeisterDataProvider {

    private final MeisterInfoRepository meisterInfoRepository;
    private final MeisterDataRepository meisterDataRepository;
    private final MeisterParsingProvider meisterProvider;
    private final MeisterAuthProvider meisterAuthProvider;

    public MeisterData findOrElseCreateMeisterData(Student student) {
        return meisterDataRepository.findById(student.getStudentId()).orElseGet(
                () -> {
                    MeisterInfo meisterInfo = meisterInfoRepository.save(
                            MeisterInfo.builder()
                                    .studentId(student.getStudentId())
                                    .lastPrivateDate(LocalDateTime.now())
                                    .build()
                    );
                    return MeisterData.builder()
                            .studentId(student.getStudentId())
                            .meisterInfo(meisterInfo)
                            .build();
                }
        );
    }

    public MeisterData getAndUpdateMeisterData(MeisterData meisterData) {
        MeisterInfo meisterInfo = meisterData.getMeisterInfo();

        MeisterDetailResponse responseDto;
        try {
            meisterData.setModifiedAt(LocalDateTime.now());
            meisterAuthProvider.login(meisterInfo.getStudent(), meisterInfo.getStudentId());
            responseDto = meisterProvider.getAllInfo(meisterInfo.getStudent());
        } catch (BadRequestException e) {
            try {
                meisterInfo.setLoginError(true);
                responseDto = meisterProvider.getScoreInfo(meisterInfo.getStudent());
                meisterData.setScore(responseDto.getScore());
                meisterData.setScoreRawData(responseDto.getScoreHtmlContent());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            meisterInfoRepository.save(meisterInfo);
            return meisterDataRepository.save(meisterData);
        } catch (HttpError e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            throw new InternalServerException();
        }

        if (meisterInfo.isLoginError()) {
            meisterInfo.setLoginError(false);
            meisterInfoRepository.save(meisterInfo);
        }
        meisterData.setScore(responseDto.getScore());
        meisterData.setScoreRawData(responseDto.getScoreHtmlContent());
        meisterData.setPositivePoint(responseDto.getPositivePoint());
        meisterData.setNegativePoint(responseDto.getNegativePoint());
        meisterData.setPointRawData(responseDto.getPointHtmlContent());

        return meisterDataRepository.save(meisterData);
    }

}
