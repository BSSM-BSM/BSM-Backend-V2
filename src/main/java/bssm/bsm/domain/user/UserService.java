package bssm.bsm.domain.user;

import bssm.bsm.domain.user.dto.teacherDto;
import bssm.bsm.domain.user.entities.Student;
import bssm.bsm.domain.user.entities.Teacher;
import bssm.bsm.domain.user.repositories.StudentRepository;
import bssm.bsm.domain.user.repositories.TeacherRepository;
import bssm.bsm.domain.user.type.UserLevel;
import bssm.bsm.domain.user.type.UserRole;
import bssm.bsm.global.error.exceptions.NotFoundException;
import bssm.bsm.domain.user.dto.BsmOauthResourceResponseDto;
import bssm.bsm.domain.user.dto.BsmOauthTokenResponseDto;
import bssm.bsm.domain.user.dto.studentDto;
import bssm.bsm.domain.user.entities.User;
import bssm.bsm.domain.user.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    private User studentSignUp(studentDto dto, String oauthToken) {
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

    private User teacherSignUp(teacherDto dto, String oauthToken) {
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

    private User studentUpdate(studentDto dto, User user) {
        Student student = user.getStudent();
        student.setGrade(dto.getGrade());
        student.setClassNo(dto.getClassNo());
        student.setStudentNo(dto.getStudentNo());
        student.setEnrolledAt(dto.getEnrolledAt());
        user.setNickname(dto.getNickname());
        return userRepository.save(user);
    }

    private User teacherUpdate(teacherDto dto, User user) {
        user.setNickname(dto.getNickname());
        return userRepository.save(user);
    }

    public User bsmOauth(String authCode) throws IOException {
        BsmOauthTokenResponseDto tokenResponseDto = bsmOauthGetToken(authCode);
        BsmOauthResourceResponseDto resourceResponseDto = bsmOauthGetResource(tokenResponseDto);

        Optional<User> user = userRepository.findById(resourceResponseDto.getUserCode());

        // SignUp
        if (user.isEmpty()) {
            return switch (resourceResponseDto.getRole()) {
                case STUDENT -> studentSignUp(resourceResponseDto.getStudent(), tokenResponseDto.getToken());
                case TEACHER -> teacherSignUp(resourceResponseDto.getTeacher(), tokenResponseDto.getToken());
            };
        }
        // Update
        return switch (resourceResponseDto.getRole()) {
            case STUDENT -> studentUpdate(resourceResponseDto.getStudent(), user.get());
            case TEACHER -> teacherUpdate(resourceResponseDto.getTeacher(), user.get());
        };
    }

    private BsmOauthTokenResponseDto bsmOauthGetToken(String authCode) throws IOException {
        // Payload
        Map<String, String> getTokenPayload = new HashMap<>();
        getTokenPayload.put("clientId", OAUTH_BSM_CLIENT_ID);
        getTokenPayload.put("clientSecret", OAUTH_BSM_CLIENT_SECRET);
        getTokenPayload.put("authCode", authCode);

        // Request
        Request tokenRequest = new Request.Builder()
                .url(OAUTH_BSM_TOKEN_URL)
                .post(RequestBody.create(objectMapper.writeValueAsString(getTokenPayload), MediaType.parse("application/json")))
                .build();
        Response tokenResponse = httpClient.newCall(tokenRequest).execute();
        if (tokenResponse.code() == 404) {
            throw new NotFoundException("인증 코드를 찾을 수 없습니다");
        }
        return objectMapper.readValue(Objects.requireNonNull(tokenResponse.body()).string(), BsmOauthTokenResponseDto.class);
    }

    private BsmOauthResourceResponseDto bsmOauthGetResource(BsmOauthTokenResponseDto dto) throws IOException {
        // Payload
        Map<String, String> getResourcePayload = new HashMap<>();
        getResourcePayload.put("clientId", OAUTH_BSM_CLIENT_ID);
        getResourcePayload.put("clientSecret", OAUTH_BSM_CLIENT_SECRET);
        getResourcePayload.put("token", dto.getToken());

        // Request
        Request resourceRequest = new Request.Builder()
                .url(OAUTH_BSM_RESOURCE_URL)
                .post(RequestBody.create(objectMapper.writeValueAsString(getResourcePayload), MediaType.parse("application/json")))
                .build();
        Response resourceResponse = httpClient.newCall(resourceRequest).execute();
        return objectMapper.readValue(Objects.requireNonNull(resourceResponse.body()).string(), BsmOauthResourceResponseDto.class);
    }
}
