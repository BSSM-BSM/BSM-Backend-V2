package bssm.bsm.domain.auth.service;

import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.domain.Teacher;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.repository.StudentRepository;
import bssm.bsm.domain.user.domain.repository.TeacherRepository;
import bssm.bsm.domain.user.domain.repository.UserRepository;
import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.domain.user.domain.type.UserRole;
import bssm.bsm.domain.user.facade.UserFacade;
import bssm.bsm.global.error.exceptions.InternalServerException;
import bssm.bsm.global.error.exceptions.NotFoundException;
import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.resource.BsmStudent;
import leehj050211.bsmOauth.dto.resource.BsmTeacher;
import leehj050211.bsmOauth.dto.resource.BsmUserResource;
import leehj050211.bsmOauth.exception.BsmOAuthCodeNotFoundException;
import leehj050211.bsmOauth.exception.BsmOAuthInvalidClientException;
import leehj050211.bsmOauth.exception.BsmOAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BsmOauthService {

    private final UserFacade userFacade;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BsmOauth bsmOauth;

    @Transactional
    private User studentSignUp(BsmUserResource resource, String oauthToken) {
        BsmStudent studentDto = resource.getStudent();
        Student student = studentRepository.findByEnrolledAtAndGradeAndClassNoAndStudentNo(
                studentDto.getEnrolledAt(),
                studentDto.getGrade(),
                studentDto.getClassNo(),
                studentDto.getStudentNo()
        ).orElseThrow(() -> new NotFoundException("학생을 찾을 수 없습니다"));

        User user = User.builder()
                .code(resource.getUserCode())
                .nickname(resource.getNickname())
                .role(UserRole.STUDENT)
                .studentId(student.getStudentId())
                .student(student)
                .level(UserLevel.USER)
                .oauthToken(oauthToken)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    private User teacherSignUp(BsmUserResource resource, String oauthToken) {
        Teacher teacher = teacherRepository.save(
                Teacher.builder()
                        .email(resource.getEmail())
                        .name(resource.getTeacher().getName())
                        .build()
        );

        User user = User.builder()
                .code(resource.getUserCode())
                .nickname(resource.getNickname())
                .role(UserRole.TEACHER)
                .teacher(teacher)
                .teacherId(teacher.getTeacherId())
                .level(UserLevel.USER)
                .oauthToken(oauthToken)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    private User studentUpdate(BsmUserResource resource, User user) {
        BsmStudent studentDto = resource.getStudent();
        Student student = user.getStudent();
        student.setGrade(studentDto.getGrade());
        student.setClassNo(studentDto.getClassNo());
        student.setStudentNo(studentDto.getStudentNo());
        student.setEnrolledAt(studentDto.getEnrolledAt());
        user.setNickname(resource.getNickname());
        return userRepository.save(user);
    }

    @Transactional
    private User teacherUpdate(BsmUserResource resource, User user) {
        BsmTeacher  teacherDto = resource.getTeacher();
        Teacher teacher = user.getTeacher();
        teacher.update(teacherDto.getName(), resource.getEmail());
        user.setNickname(resource.getNickname());
        return userRepository.save(user);
    }

    public User bsmOauth(String authCode) throws IOException {
        String token;
        BsmUserResource resource;
        try {
            token = bsmOauth.getToken(authCode);
            resource = bsmOauth.getResource(token);
        } catch (BsmOAuthCodeNotFoundException e) {
            throw new NotFoundException("인증 코드를 찾을 수 없습니다");
        } catch (BsmOAuthTokenNotFoundException e) {
            throw new NotFoundException("토큰을 찾을 수 없습니다");
        } catch (BsmOAuthInvalidClientException e) {
            throw new InternalServerException();
        }

        User user = userFacade.findByCodeOrNull(resource.getUserCode());

        // SignUp
        if (user == null) {
            return switch (resource.getRole()) {
                case STUDENT -> studentSignUp(resource, token);
                case TEACHER -> teacherSignUp(resource, token);
            };
        }
        // Update
        return switch (resource.getRole()) {
            case STUDENT -> studentUpdate(resource, user);
            case TEACHER -> teacherUpdate(resource, user);
        };
    }

}
