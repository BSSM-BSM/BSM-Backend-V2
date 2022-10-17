package bssm.bsm.domain.user.service;

import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.domain.Teacher;
import bssm.bsm.domain.user.domain.StudentRepository;
import bssm.bsm.domain.user.domain.TeacherRepository;
import bssm.bsm.domain.user.domain.UserLevel;
import bssm.bsm.domain.user.domain.UserRole;
import bssm.bsm.domain.user.presentation.dto.response.UserInfoResponse;
import bssm.bsm.global.error.exceptions.InternalServerException;
import bssm.bsm.global.error.exceptions.NotFoundException;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.UserRepository;
import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import leehj050211.bsmOauth.dto.response.BsmStudentResponse;
import leehj050211.bsmOauth.exceptions.BsmAuthCodeNotFoundException;
import leehj050211.bsmOauth.exceptions.BsmAuthInvalidClientException;
import leehj050211.bsmOauth.exceptions.BsmAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BsmOauth bsmOauth;

    private User studentSignUp(BsmResourceResponse dto, String oauthToken) {
        BsmStudentResponse studentDto = dto.getStudent();
        Student student = studentRepository.findByEnrolledAtAndGradeAndClassNoAndStudentNo(
                studentDto.getEnrolledAt(),
                studentDto.getGrade(),
                studentDto.getClassNo(),
                studentDto.getStudentNo()
        ).orElseThrow(
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
