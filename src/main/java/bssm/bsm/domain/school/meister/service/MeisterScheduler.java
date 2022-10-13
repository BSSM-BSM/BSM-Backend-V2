package bssm.bsm.domain.school.meister.service;

import bssm.bsm.domain.school.meister.entities.MeisterData;
import bssm.bsm.domain.school.meister.repositories.MeisterDataRepository;
import bssm.bsm.domain.user.entities.Student;
import bssm.bsm.domain.user.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MeisterScheduler {

    private final StudentRepository studentRepository;
    private final MeisterDataRepository meisterDataRepository;
    private final MeisterDataProvider meisterDataProvider;

    @Scheduled(cron = "0 0 0 * * ?")
    private void updateAllStudentsInfo() {
        // 재학중인 학생 리스트 불러오기
        List<Student> studentList = studentRepository.findByGradeNot(0);
        List<MeisterData> meisterDataList = meisterDataRepository.findAll();

        studentList.forEach(student -> {
            // 이미 정보가 저장되어있는 학생인지 확인
            Optional<MeisterData> data = meisterDataList.stream()
                    .filter(meisterData -> meisterData.getStudentId().equals(student.getStudentId()))
                    .findFirst();

            MeisterData meisterData = data.orElseGet(
                    () -> meisterDataProvider.findOrElseCreateMeisterData(student)
            );
            meisterData.getMeisterInfo().setStudent(student);

            // 정보 업데이트
            meisterDataProvider.getAndUpdateMeisterData(meisterData);
            try {
                // 마이스터 인증제 서버에 부담이 가지않도록 1초 지연
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}
