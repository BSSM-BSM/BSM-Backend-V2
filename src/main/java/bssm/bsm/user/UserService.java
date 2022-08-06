package bssm.bsm.user;

import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.user.dto.BsmOauthResourceResponseDto;
import bssm.bsm.user.dto.BsmOauthTokenResponseDto;
import bssm.bsm.user.dto.UserSignUpDto;
import bssm.bsm.user.entities.Student;
import bssm.bsm.user.entities.User;
import bssm.bsm.user.repositories.StudentRepository;
import bssm.bsm.user.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${OAUTH_BSM_CLIENT_ID}")
    private String OAUTH_BSM_CLIENT_ID;
    @Value("${OAUTH_BSM_CLIENT_SECRET}")
    private String OAUTH_BSM_CLIENT_SECRET;
    @Value("${OAUTH_BSM_TOKEN_URL}")
    private String OAUTH_BSM_TOKEN_URL;
    @Value("${OAUTH_BSM_RESOURCE_URL}")
    private String OAUTH_BSM_RESOURCE_URL;

    @Transactional
    public User signUp(UserSignUpDto dto, String oauthToken) {
        Student student = studentRepository.findByEnrolledAtAndGradeAndClassNoAndStudentNo(dto.getEnrolledAt(), dto.getGrade(), dto.getClassNo(), dto.getStudentNo()).orElseThrow(
                () -> {throw new NotFoundException("학생을 찾을 수 없습니다");}
        );
        User user = User.builder()
                .usercode(dto.getUsercode())
                .nickname(dto.getNickname())
                .uniqNo(student.getUniqNo())
                .student(student)
                .level(student.getLevel())
                .oauthToken(oauthToken)
                .build();
        userRepository.save(user);

        return user;
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
        BsmOauthTokenResponseDto tokenResponseDto = objectMapper.readValue(tokenResponse.body().string(), BsmOauthTokenResponseDto.class);

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
        BsmOauthResourceResponseDto resourceResponseDto = objectMapper.readValue(resourceResponse.body().string(), BsmOauthResourceResponseDto.class);

        Optional<User> user = userRepository.findById(resourceResponseDto.getUser().getUsercode());

        // SignUp
        if (user.isEmpty()) {
            return signUp(resourceResponseDto.getUser(), tokenResponseDto.getToken());
        }
        return user.get();
    }
}
