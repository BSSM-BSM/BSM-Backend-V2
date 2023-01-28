package bssm.bsm.domain.auth.service;

import bssm.bsm.domain.auth.domain.repository.RefreshTokenRepository;
import bssm.bsm.domain.auth.presentation.dto.res.AuthTokenRes;
import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.domain.Teacher;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.repository.StudentRepository;
import bssm.bsm.domain.user.domain.repository.TeacherRepository;
import bssm.bsm.domain.user.domain.repository.UserRepository;
import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.domain.user.domain.type.UserRole;
import bssm.bsm.global.error.exceptions.InternalServerException;
import bssm.bsm.global.error.exceptions.NotFoundException;
import bssm.bsm.global.jwt.JwtProvider;
import bssm.bsm.global.utils.CookieUtil;
import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import leehj050211.bsmOauth.dto.response.BsmStudentResponse;
import leehj050211.bsmOauth.exceptions.BsmAuthCodeNotFoundException;
import leehj050211.bsmOauth.exceptions.BsmAuthInvalidClientException;
import leehj050211.bsmOauth.exceptions.BsmAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BsmOauthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BsmOauth bsmOauth;

    @Transactional
    private User studentSignUp(BsmResourceResponse dto, String oauthToken) {
        BsmStudentResponse studentDto = dto.getStudent();
        Student student = studentRepository.findByEnrolledAtAndGradeAndClassNoAndStudentNo(
                studentDto.getEnrolledAt(),
                studentDto.getGrade(),
                studentDto.getClassNo(),
                studentDto.getStudentNo()
        ).orElseThrow(() -> new NotFoundException("학생을 찾을 수 없습니다"));

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
    private User teacherSignUp(BsmResourceResponse dto, String oauthToken) {
        Teacher teacher = teacherRepository.save(
                Teacher.builder()
                        .email(dto.getEmail())
                        .name(dto.getTeacher().getName())
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

    @Transactional
    private User studentUpdate(BsmResourceResponse dto, User user) {
        BsmStudentResponse studentDto = dto.getStudent();
        Student student = user.getStudent();
        student.setGrade(studentDto.getGrade());
        student.setClassNo(studentDto.getClassNo());
        student.setStudentNo(studentDto.getStudentNo());
        student.setEnrolledAt(studentDto.getEnrolledAt());
        user.setNickname(dto.getNickname());
        return userRepository.save(user);
    }

    @Transactional
    private User teacherUpdate(BsmResourceResponse dto, User user) {
        user.setNickname(dto.getNickname());
        return userRepository.save(user);
    }

    public User bsmOauth(String authCode) throws IOException {
        String token;
        BsmResourceResponse resource;
        try {
            token = bsmOauth.getToken(authCode);
            resource = bsmOauth.getResource(token);
        } catch (BsmAuthCodeNotFoundException e) {
            throw new NotFoundException("인증 코드를 찾을 수 없습니다");
        } catch (BsmAuthTokenNotFoundException e) {
            throw new NotFoundException("토큰을 찾을 수 없습니다");
        } catch (BsmAuthInvalidClientException e) {
            throw new InternalServerException();
        }

        Optional<User> user = userRepository.findById(resource.getUserCode());

        // SignUp
        if (user.isEmpty()) {
            return switch (resource.getRole()) {
                case STUDENT -> studentSignUp(resource, token);
                case TEACHER -> teacherSignUp(resource, token);
            };
        }
        // Update
        return switch (resource.getRole()) {
            case STUDENT -> studentUpdate(resource, user.get());
            case TEACHER -> teacherUpdate(resource, user.get());
        };
    }

}
