package bssm.bsm.domain.user;

import bssm.bsm.domain.user.dto.teacherSignUpDto;
import bssm.bsm.domain.user.entities.Student;
import bssm.bsm.domain.user.entities.Teacher;
import bssm.bsm.domain.user.repositories.StudentRepository;
import bssm.bsm.domain.user.repositories.TeacherRepository;
import bssm.bsm.domain.user.type.UserLevel;
import bssm.bsm.domain.user.type.UserRole;
import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.domain.user.dto.BsmOauthResourceResponseDto;
import bssm.bsm.domain.user.dto.BsmOauthTokenResponseDto;
import bssm.bsm.domain.user.dto.studentSignUpDto;
import bssm.bsm.domain.user.entities.User;
import bssm.bsm.domain.user.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${env.oauth.bsm.client.id}")
    private String OAUTH_BSM_CLIENT_ID;
    @Value("${env.oauth.bsm.client.secret}")
    private String OAUTH_BSM_CLIENT_SECRET;
    @Value("${env.oauth.bsm.url.token}")
    private String OAUTH_BSM_TOKEN_URL;
    @Value("${env.oauth.bsm.url.resource}")
    private String OAUTH_BSM_RESOURCE_URL;

    @Transactional
    private User studentSignUp(studentSignUpDto dto, String oauthToken) {
        Student student = studentRepository.findByEnrolledAtAndGradeAndClassNoAndStudentNo(dto.getEnrolledAt(), dto.getGrade(), dto.getClassNo(), dto.getStudentNo()).orElseThrow(
                () -> {throw new NotFoundException("학생을 찾을 수 없습니다");}
        );
        User user = User.builder()
                .code(dto.getUserCode())
                .nickname(dto.getNickname())
                .role(UserRole.STUDENT)
                .studentId(student.getStudentId())
                .student(student)
                .level(UserLevel.USER)
                .oauthToken(oauthToken)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    private User teacherSignUp(teacherSignUpDto dto, String oauthToken) {
        Teacher teacher = teacherRepository.save(
                Teacher.builder()
                        .email(dto.getEmail())
                        .name(dto.getName())
                        .build()
        );
        User user = User.builder()
                .code(dto.getUserCode())
                .nickname(dto.getNickname())
                .role(UserRole.TEACHER)
                .teacher(teacher)
                .teacherId(teacher.getTeacherId())
                .level(UserLevel.USER)
                .oauthToken(oauthToken)
                .build();

        return userRepository.save(user);
    }

    public User bsmOauth(String authCode) throws IOException {
        // Payload
        Map<String, String> getTokenPayload = new HashMap<>();
        getTokenPayload.put("clientId", OAUTH_BSM_CLIENT_ID);
        getTokenPayload.put("clientSecret", OAUTH_BSM_CLIENT_SECRET);
        getTokenPayload.put("authCode", authCode);

        // Request
        Request tokenRequest = new Request.Builder()
                .url(OAUTH_BSM_TOKEN_URL)
                .post(RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(getTokenPayload)))
                .build();
        Response tokenResponse = httpClient.newCall(tokenRequest).execute();
        if (tokenResponse.code() == 404) {
            throw new NotFoundException("인증 코드를 찾을 수 없습니다");
        }
        BsmOauthTokenResponseDto tokenResponseDto = objectMapper.readValue(Objects.requireNonNull(tokenResponse.body()).string(), BsmOauthTokenResponseDto.class);

        // Payload
        Map<String, String> getResourcePayload = new HashMap<>();
        getResourcePayload.put("clientId", OAUTH_BSM_CLIENT_ID);
        getResourcePayload.put("clientSecret", OAUTH_BSM_CLIENT_SECRET);
        getResourcePayload.put("token", tokenResponseDto.getToken());

        // Request
        Request resourceRequest = new Request.Builder()
                .url(OAUTH_BSM_RESOURCE_URL)
                .post(RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(getResourcePayload)))
                .build();
        Response resourceResponse = httpClient.newCall(resourceRequest).execute();
        String str = Objects.requireNonNull(resourceResponse.body()).string();
        BsmOauthResourceResponseDto resourceResponseDto = objectMapper.readValue(str, BsmOauthResourceResponseDto.class);
        System.out.println(str);

        Optional<User> user = userRepository.findById(resourceResponseDto.getUserCode());

        // SignUp
        if (user.isEmpty()) {
            return switch (resourceResponseDto.getRole()) {
                case STUDENT -> studentSignUp(resourceResponseDto.getStudent(), tokenResponseDto.getToken());
                case TEACHER -> teacherSignUp(resourceResponseDto.getTeacher(), tokenResponseDto.getToken());
            };
        }
        return user.get();
    }
}
