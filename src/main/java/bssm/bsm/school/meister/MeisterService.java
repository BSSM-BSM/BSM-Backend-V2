package bssm.bsm.school.meister;

import bssm.bsm.global.exceptions.BadRequestException;
import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.school.meister.dto.request.GetMeisterPointDto;
import bssm.bsm.school.meister.dto.response.MeisterScoreAndPointResponseDto;
import bssm.bsm.user.entities.Student;
import bssm.bsm.user.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MeisterService {

    private final StudentRepository studentRepository;
    private final OkHttpClient httpClient;
    private final String GET_SCORE_URL = "https://bssm.meistergo.co.kr/_suCert/bssm/B002/jnv_201j.php";
    private final String GET_POINT_URL = "https://bssm.meistergo.co.kr/ss/ss_a40j.php";
    private final String LOGIN_URL = "https://bssm.meistergo.co.kr/inc/common_json.php";
    private final String LOGOUT_URL = "https://bssm.meistergo.co.kr/logout.php";

    public MeisterScoreAndPointResponseDto getScoreAndPoint(GetMeisterPointDto dto) throws IOException {
        Student student = studentRepository.findByGradeAndClassNoAndStudentNo(dto.getGrade(), dto.getClassNo(), dto.getStudentNo()).orElseThrow(
                () -> {throw new NotFoundException("학생을 찾을 수 없습니다");}
        );

        login(student, dto.getPw().isEmpty()? student.getUniqNo(): dto.getPw());

        String scoreHtmlContent = getScore(student);
        String pointHtmlContent = getPoint();

        int positivePoint = 0;
        int negativePoint = 0;

        Matcher positiveMatch = Pattern.compile("(\\(상점 : [0-9]*)").matcher(pointHtmlContent);
        Matcher negativeMatch = Pattern.compile("(\\(벌점 : [0-9]*)").matcher(pointHtmlContent);

        while (positiveMatch.find()) {
            positivePoint += Integer.parseInt(positiveMatch.group().split(" ")[2]);
        }
        while (negativeMatch.find()) {
            negativePoint += Integer.parseInt(negativeMatch.group().split(" ")[2]);
        }

        logout();
        return MeisterScoreAndPointResponseDto.builder()
                .scoreHtmlContent(scoreHtmlContent)
                .pointHtmlContent(pointHtmlContent)
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
