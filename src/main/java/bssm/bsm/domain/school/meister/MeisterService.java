package bssm.bsm.domain.school.meister;

import bssm.bsm.domain.school.meister.dto.response.MeisterRankingDto;
import bssm.bsm.domain.school.meister.dto.response.MeisterStudentResponseDto;
import bssm.bsm.domain.school.meister.entities.MeisterData;
import bssm.bsm.domain.school.meister.entities.MeisterInfo;
import bssm.bsm.domain.school.meister.repositories.MeisterDataRepository;
import bssm.bsm.domain.school.meister.type.MeisterInfoResultType;
import bssm.bsm.domain.user.entities.Student;
import bssm.bsm.domain.user.entities.User;
import bssm.bsm.domain.user.repositories.StudentRepository;
import bssm.bsm.global.exceptions.BadRequestException;
import bssm.bsm.global.exceptions.HttpError;
import bssm.bsm.global.exceptions.InternalServerException;
import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.domain.school.meister.dto.request.MeisterDetailRequestDto;
import bssm.bsm.domain.school.meister.dto.response.MeisterDetailResponseDto;
import bssm.bsm.domain.school.meister.dto.response.MeisterResponseDto;
import bssm.bsm.domain.school.meister.repositories.MeisterInfoRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeisterService {

    private final StudentRepository studentRepository;
    private final MeisterInfoRepository meisterInfoRepository;
    private final MeisterDataRepository meisterDataRepository;
    private final OkHttpClient httpClient;
    private final String GET_SCORE_URL = "https://bssm.meistergo.co.kr/_suCert/bssm/B002/jnv_201j.php";
    private final String GET_POINT_URL = "https://bssm.meistergo.co.kr/ss/ss_a40j.php";
    private final String LOGIN_URL = "https://bssm.meistergo.co.kr/inc/common_json.php";
    private final String LOGOUT_URL = "https://bssm.meistergo.co.kr/logout.php";

    public void updatePrivateRanking(User user, boolean privateRanking) {
        MeisterInfo meisterInfo = meisterInfoRepository.findById(user.getStudentId()).orElseThrow(
                () -> {throw new NotFoundException("마이스터 정보를 가져올 수 없습니다");}
        );

        meisterInfo.setPrivateRanking(privateRanking);
        meisterInfoRepository.save(meisterInfo);
    }

    public MeisterDetailResponseDto getDetail(MeisterDetailRequestDto dto) throws IOException {
        Student student = studentRepository.findByGradeAndClassNoAndStudentNo(dto.getGrade(), dto.getClassNo(), dto.getStudentNo()).orElseThrow(
                () -> {throw new NotFoundException("학생을 찾을 수 없습니다");}
        );

        login(student, dto.getPw().isEmpty()? student.getStudentId(): dto.getPw());

        MeisterDetailResponseDto detailInfo = getAllInfo(student);

        MeisterData meisterData = findOrCreateMeisterData(student);
        meisterData.setScore(detailInfo.getScore());
        meisterData.setScoreRawData(detailInfo.getScoreHtmlContent());
        meisterData.setPositivePoint(detailInfo.getPositivePoint());
        meisterData.setNegativePoint(detailInfo.getNegativePoint());
        meisterData.setPointRawData(detailInfo.getPointHtmlContent());
        meisterDataRepository.save(meisterData);

        return detailInfo;
    }

    public MeisterResponseDto get(User user) {
        MeisterData meisterData = meisterDataRepository.findByStudentIdAndModifiedAtGreaterThan(user.getStudentId(), LocalDate.now().atStartOfDay()).orElseGet(
                () -> getAndUpdateMeisterData(findOrCreateMeisterData(user.getStudent()))
        );
        MeisterInfo meisterInfo = meisterData.getMeisterInfo();

        if (meisterInfo.isLoginError()) {
            return MeisterResponseDto.builder()
                    .uniqNo(meisterInfo.getStudentId())
                    .lastUpdate(LocalDateTime.now())
                    .loginError(true)
                    .build();
        }

        return MeisterResponseDto.builder()
                .score(meisterData.getScore())
                .positivePoint(meisterData.getPositivePoint())
                .negativePoint(meisterData.getNegativePoint())
                .lastUpdate(meisterData.getModifiedAt())
                .loginError(false)
                .build();
    }

    public MeisterResponseDto updateAndGet(User user) {
        MeisterData meisterData = getAndUpdateMeisterData(findOrCreateMeisterData(user.getStudent()));
        MeisterInfo meisterInfo = meisterData.getMeisterInfo();

        if (meisterInfo.isLoginError()) {
            return MeisterResponseDto.builder()
                    .uniqNo(meisterInfo.getStudentId())
                    .lastUpdate(LocalDateTime.now())
                    .loginError(true)
                    .build();
        }

        return MeisterResponseDto.builder()
                .score(meisterData.getScore())
                .positivePoint(meisterData.getPositivePoint())
                .negativePoint(meisterData.getNegativePoint())
                .lastUpdate(meisterData.getModifiedAt())
                .loginError(false)
                .build();
    }

    @Transactional
    private MeisterData findOrCreateMeisterData(Student student) {
        return meisterDataRepository.findById(student.getStudentId()).orElseGet(
                () -> {
                    MeisterInfo meisterInfo = meisterInfoRepository.save(
                            MeisterInfo.builder()
                                    .studentId(student.getStudentId())
                                    .student(student)
                                    .loginError(true)
                                    .build()
                    );
                    System.out.println("meisterInfo.getStudentId() = " + meisterInfo.getStudentId());
                    return MeisterData.builder()
                            .meisterId(meisterInfo.getStudentId())
                            .build();
                }
        );
    }

    private MeisterData getAndUpdateMeisterData(MeisterData meisterData) {
        MeisterInfo meisterInfo = meisterData.getMeisterInfo();

        MeisterDetailResponseDto responseDto;
        try {
            login(meisterData.getStudent(), meisterData.getStudentId());
            responseDto = getAllInfo(meisterData.getStudent());
        } catch (BadRequestException e) {
            meisterInfo.setLoginError(true);
            meisterInfoRepository.save(meisterInfo);
            return meisterData;
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

    public List<MeisterRankingDto> getRanking() {
        return meisterDataRepository.findByOrderByScoreDesc().stream()
                .map(meisterData -> {
                    Student student = meisterData.getStudent();
                    MeisterRankingDto.MeisterRankingDtoBuilder builder = MeisterRankingDto.builder()
                            .student(MeisterStudentResponseDto.builder()
                                    .grade(student.getGrade())
                                    .classNo(student.getClassNo())
                                    .studentNo(student.getStudentNo())
                                    .name(student.getName())
                                    .build()
                            )
                            .result(convertResult(meisterData.getMeisterInfo()));

                    if (meisterData.getMeisterInfo().isPrivateRanking()) {
                        return builder.build();
                    }

                    return builder
                            .score(meisterData.getScore())
                            .positivePoint(meisterData.getPositivePoint())
                            .negativePoint(meisterData.getNegativePoint())
                            .lastUpdate(meisterData.getModifiedAt())
                            .build();
                    }
                ).collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void updateAllStudentsInfo() {
        // 재학중인 학생 리스트 불러오기
        List<Student> studentList = studentRepository.findByGradeNot(0);
        List<MeisterData> meisterDataList = meisterDataRepository.findAll();

        studentList.forEach(student -> {
            // 이미 정보가 저장되어있는 학생인지 확인
            Optional<MeisterData> data = meisterDataList.stream()
                    .filter(meisterData -> meisterData.getStudent().equals(student))
                    .findFirst();

            // 정보를 자동으로 불러올 수 없다면 다음 학생 불러옴
            if (data.isPresent() && data.get().getMeisterInfo().isLoginError()) return;

            // 정보 업데이트
            getAndUpdateMeisterData(
                    data.orElseGet(
                        () -> findOrCreateMeisterData(student)
                    )
            );
            try {
                // 마이스터 인증제 서버에 부담이 가지않도록 1초 지연
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private MeisterDetailResponseDto getAllInfo(Student student) throws IOException {
        String scoreHtmlContent = getScore(student);
        String pointHtmlContent = getPoint();

        float score = 0;
        int positivePoint = 0;
        int negativePoint = 0;

        Matcher scoreMatch = Pattern.compile("<td>[\\d.]*<\\/td>").matcher(scoreHtmlContent);
        Matcher positiveMatch = Pattern.compile("(\\(상점 : [0-9]*)").matcher(pointHtmlContent);
        Matcher negativeMatch = Pattern.compile("(\\(벌점 : [0-9]*)").matcher(pointHtmlContent);

        if (scoreMatch.find()) {
            score = Float.parseFloat(scoreMatch.group().split("<")[1].substring(3));
        }
        while (positiveMatch.find()) {
            positivePoint += Integer.parseInt(positiveMatch.group().split(" ")[2]);
        }
        while (negativeMatch.find()) {
            negativePoint += Integer.parseInt(negativeMatch.group().split(" ")[2]);
        }

        logout();
        return MeisterDetailResponseDto.builder()
                .scoreHtmlContent(scoreHtmlContent)
                .pointHtmlContent(pointHtmlContent)
                .score(score)
                .positivePoint(positivePoint)
                .negativePoint(negativePoint)
                .build();
    }

    private String getScore(Student student) throws IOException {
        Request request = new Request.Builder()
                .url(GET_SCORE_URL)
                .post(new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("caseBy", "getViewer")
                        .addFormDataPart("uniqNo", student.getStudentId())
                        .build()
                )
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    private String getPoint() throws IOException {
        Request request = new Request.Builder()
                .url(GET_POINT_URL)
                .post(new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("caseBy", "listview")
                        .addFormDataPart("pageNumber", "1")
                        .addFormDataPart("onPageCnt", "100")
                        .build()
                )
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    private void login(Student student, String pw) throws IOException {
        String hakgwa;
        if (student.getGrade() == 1) {
            hakgwa = "공통과정";
        } else if (student.getClassNo() <= 2) {
            hakgwa = "소프트웨어개발과";
        } else {
            hakgwa = "임베디드소프트웨어과";
        }
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("caseBy", "login")
                        .addFormDataPart("pw", pw)
                        .addFormDataPart("lgtype", "S")
                        .addFormDataPart("hakgwa", hakgwa)
                        .addFormDataPart("hak", String.valueOf(student.getGrade()))
                        .addFormDataPart("ban", String.valueOf(student.getClassNo()))
                        .addFormDataPart("bun", String.valueOf(student.getStudentNo()))
                        .build())
                .build();

        Response response = httpClient.newCall(request).execute();
        if (!response.body().string().equals("true")) {
            throw new BadRequestException("비밀번호가 맞지 않습니다. 다른 비밀번호로 시도해 보세요.");
        }
    }

    private void logout() throws IOException {
        httpClient.newCall(
                new Request.Builder()
                        .url(LOGOUT_URL)
                        .get()
                        .build()
        ).execute();
    }

    private MeisterInfoResultType convertResult(MeisterInfo meisterInfo) {
        if (meisterInfo.isPrivateRanking()) return MeisterInfoResultType.PRIVATE;
        if (meisterInfo.isLoginError()) return MeisterInfoResultType.LOGIN_ERROR;
        return MeisterInfoResultType.SUCCESS;
    }
}
