package bssm.bsm.school.meister;

import bssm.bsm.global.exceptions.BadRequestException;
import bssm.bsm.global.exceptions.HttpError;
import bssm.bsm.global.exceptions.InternalServerException;
import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.school.meister.dto.request.MeisterDetailRequestDto;
import bssm.bsm.school.meister.dto.response.MeisterDetailResponseDto;
import bssm.bsm.school.meister.dto.response.MeisterRankingDto;
import bssm.bsm.school.meister.dto.response.MeisterResponseDto;
import bssm.bsm.school.meister.entities.MeisterInfo;
import bssm.bsm.school.meister.repositories.MeisterInfoRepository;
import bssm.bsm.user.entities.Student;
import bssm.bsm.user.entities.User;
import bssm.bsm.user.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
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
    private final OkHttpClient httpClient;
    private final String GET_SCORE_URL = "https://bssm.meistergo.co.kr/_suCert/bssm/B002/jnv_201j.php";
    private final String GET_POINT_URL = "https://bssm.meistergo.co.kr/ss/ss_a40j.php";
    private final String LOGIN_URL = "https://bssm.meistergo.co.kr/inc/common_json.php";
    private final String LOGOUT_URL = "https://bssm.meistergo.co.kr/logout.php";

    public MeisterDetailResponseDto getDetail(MeisterDetailRequestDto dto) throws IOException {
        Student student = studentRepository.findByGradeAndClassNoAndStudentNo(dto.getGrade(), dto.getClassNo(), dto.getStudentNo()).orElseThrow(
                () -> {throw new NotFoundException("학생을 찾을 수 없습니다");}
        );

        login(student, dto.getPw().isEmpty()? student.getUniqNo(): dto.getPw());

        MeisterDetailResponseDto detailInfo = getAllInfo(student);

        meisterInfoRepository.save(
                MeisterInfo.builder()
                        .uniqNo(student.getUniqNo())
                        .score(detailInfo.getScore())
                        .scoreRawData(detailInfo.getScoreHtmlContent())
                        .positivePoint(detailInfo.getPositivePoint())
                        .negativePoint(detailInfo.getNegativePoint())
                        .pointRawData(detailInfo.getPointHtmlContent())
                        .build()
        );

        return detailInfo;
    }

    public MeisterResponseDto get(User user) {
        MeisterInfo meisterInfo = meisterInfoRepository.findByUniqNoAndModifiedAtGreaterThan(user.getUniqNo(), LocalDate.now().atStartOfDay()).orElseGet(
                () -> getAndUpdateMeisterInfo(user.getStudent())
        );

        if (meisterInfo.isLoginError()) {
            return MeisterResponseDto.builder()
                    .uniqNo(meisterInfo.getUniqNo())
                    .lastUpdate(meisterInfo.getModifiedAt())
                    .loginError(true)
                    .build();
        }

        return MeisterResponseDto.builder()
                .score(meisterInfo.getScore())
                .positivePoint(meisterInfo.getPositivePoint())
                .negativePoint(meisterInfo.getNegativePoint())
                .lastUpdate(meisterInfo.getModifiedAt())
                .loginError(false)
                .build();
    }

    public MeisterResponseDto updateAndGet(User user) {
        MeisterInfo meisterInfo = getAndUpdateMeisterInfo(user.getStudent());

        if (meisterInfo.isLoginError()) {
            return MeisterResponseDto.builder()
                    .uniqNo(meisterInfo.getUniqNo())
                    .lastUpdate(meisterInfo.getModifiedAt())
                    .loginError(true)
                    .build();
        }

        return MeisterResponseDto.builder()
                .score(meisterInfo.getScore())
                .positivePoint(meisterInfo.getPositivePoint())
                .negativePoint(meisterInfo.getNegativePoint())
                .lastUpdate(meisterInfo.getModifiedAt())
                .loginError(false)
                .build();
    }

    private MeisterInfo getAndUpdateMeisterInfo(Student student) {
        MeisterDetailResponseDto responseDto;
        MeisterInfo.MeisterInfoBuilder entity = MeisterInfo.builder();
        try {
            login(student, student.getUniqNo());
            responseDto = getAllInfo(student);
        } catch (BadRequestException e) {
            responseDto = MeisterDetailResponseDto.builder()
                    .score(0)
                    .positivePoint(0)
                    .negativePoint(0)
                    .build();
            entity = entity.loginError(true);
        } catch (HttpError e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            throw new InternalServerException();
        }

        return meisterInfoRepository.save(
                entity
                        .uniqNo(student.getUniqNo())
                        .score(responseDto.getScore())
                        .scoreRawData(responseDto.getScoreHtmlContent())
                        .positivePoint(responseDto.getPositivePoint())
                        .negativePoint(responseDto.getNegativePoint())
                        .pointRawData(responseDto.getPointHtmlContent())
                        .build()
        );
    }

    public List<MeisterRankingDto> getRanking() {
        return meisterInfoRepository.findByOrderByScoreDesc().stream()
                .map(meisterInfo -> {
                    Student student = meisterInfo.getStudent();
                    return MeisterRankingDto.builder()
                            .score(meisterInfo.getScore())
                            .positivePoint(meisterInfo.getPositivePoint())
                            .negativePoint(meisterInfo.getNegativePoint())
                            .lastUpdate(meisterInfo.getModifiedAt())
                            .student(Student.builder()
                                    .grade(student.getGrade())
                                    .classNo(student.getClassNo())
                                    .studentNo(student.getStudentNo())
                                    .name(student.getName())
                                    .build()
                            )
                            .loginError(meisterInfo.isLoginError())
                            .build();
                    }
                ).collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void updateAllStudentsInfo() {
        // 재학중인 학생 리스트 불러오기
        List<Student> studentList = studentRepository.findByGradeNot(0);
        List<MeisterInfo> meisterInfoList = meisterInfoRepository.findAll();

        studentList.forEach(student -> {
            // 이미 정보가 저장되어있는 학생인지 확인
            Optional<MeisterInfo> info = meisterInfoList.stream()
                    .filter(meisterInfo -> meisterInfo.getStudent().equals(student))
                    .findFirst();

            // 정보를 자동으로 불러올 수 없다면 다음 학생 불러옴
            if (info.isPresent() && info.get().isLoginError()) return;

            // 정보 업데이트
            getAndUpdateMeisterInfo(student);
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

        int score = 0;
        int positivePoint = 0;
        int negativePoint = 0;

        Matcher scoreMatch = Pattern.compile("<td>\\d*<\\/td>").matcher(scoreHtmlContent);
        Matcher positiveMatch = Pattern.compile("(\\(상점 : [0-9]*)").matcher(pointHtmlContent);
        Matcher negativeMatch = Pattern.compile("(\\(벌점 : [0-9]*)").matcher(pointHtmlContent);

        if (scoreMatch.find()) {
            score = Integer.parseInt(scoreMatch.group().split("<")[1].substring(3));
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
                        .addFormDataPart("uniqNo", student.getUniqNo())
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
}
