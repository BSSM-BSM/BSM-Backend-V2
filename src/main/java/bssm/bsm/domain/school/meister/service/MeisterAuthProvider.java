package bssm.bsm.domain.school.meister.service;

import bssm.bsm.domain.user.entities.Student;
import bssm.bsm.global.error.exceptions.BadRequestException;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MeisterAuthProvider {

    private final OkHttpClient httpClient;
    private final String LOGIN_URL = "https://bssm.meistergo.co.kr/inc/common_json.php";
    private final String LOGOUT_URL = "https://bssm.meistergo.co.kr/logout.php";

    public void login(Student student, String pw) throws IOException {
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
        if (!Objects.requireNonNull(response.body()).string().equals("true")) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("pw", "비밀번호가 맞지 않습니다. 다른 비밀번호로 시도해 보세요").
                    build()
            );
        }
    }

    public void logout() throws IOException {
        httpClient.newCall(
                new Request.Builder()
                        .url(LOGOUT_URL)
                        .get()
                        .build()
        ).execute();
    }

}
