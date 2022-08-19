package bssm.bsm.school.meister;

import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.school.meister.dto.request.FindStudentInfoDto;
import bssm.bsm.user.entities.Student;
import bssm.bsm.user.repositories.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MeisterService {

    private final StudentRepository studentRepository;
    private final OkHttpClient httpClient;
    private final String GET_SCORE_URL = "https://bssm.meistergo.co.kr/_suCert/bssm/B002/jnv_201j.php";

    public String getScore(FindStudentInfoDto dto) throws IOException {
        Student student = studentRepository.findByGradeAndClassNoAndStudentNo(dto.getGrade(), dto.getClassNo(), dto.getStudentNo()).orElseThrow(
                () -> {throw new NotFoundException("학생을 찾을 수 없습니다");}
        );

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
}
