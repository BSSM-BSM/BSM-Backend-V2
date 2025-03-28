package bssm.bsm.domain.auth.service;

import bssm.bsm.domain.auth.exception.NoSuchBsmAuthCodeException;
import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.domain.Teacher;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.repository.TeacherRepository;
import bssm.bsm.domain.user.domain.repository.UserRepository;
import bssm.bsm.domain.user.facade.StudentFacade;
import bssm.bsm.domain.user.facade.UserFacade;
import bssm.bsm.global.error.exceptions.InternalServerException;
import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.resource.BsmStudent;
import leehj050211.bsmOauth.dto.resource.BsmTeacher;
import leehj050211.bsmOauth.dto.resource.BsmUserResource;
import leehj050211.bsmOauth.exception.BsmOAuthCodeNotFoundException;
import leehj050211.bsmOauth.exception.BsmOAuthInvalidClientException;
import leehj050211.bsmOauth.exception.BsmOAuthTokenNotFoundException;
import leehj050211.bsmOauth.type.BsmUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BsmOauthService {

    private final UserFacade userFacade;
    private final StudentFacade studentFacade;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final BsmOauth bsmOauth;

    @Transactional
    public User bsmOauth(String authCode) throws IOException {
        String token;
        BsmUserResource resource;
        try {
            token = bsmOauth.getToken(authCode);
            resource = bsmOauth.getResource(token);
        } catch (BsmOAuthCodeNotFoundException | BsmOAuthTokenNotFoundException e) {
            throw new NoSuchBsmAuthCodeException();
        } catch (BsmOAuthInvalidClientException e) {
            throw new InternalServerException();
        }

        User user = userFacade.findByCodeOrNull(resource.getId());
        return signUpOrUpdate(user, resource, token);
    }

    private User signUpOrUpdate(User user, BsmUserResource resource, String token) {
        if (user == null) {
            return signUp(resource, token);
        }

        if (Objects.equals(resource.getRole(), BsmUserRole.STUDENT)) {
            studentUpdate(resource, user.getStudent());
        }
        if (Objects.equals(resource.getRole(), BsmUserRole.TEACHER)) {
            teacherUpdate(resource, user.getTeacher());
        }
        user.update(resource.getNickname());
        return user;
    }

    private User signUp(BsmUserResource resource, String token) {
        if (Objects.equals(resource.getRole(), BsmUserRole.STUDENT)) {
            return studentSignUp(resource, token);
        }
        if (Objects.equals(resource.getRole(), BsmUserRole.TEACHER)) {
            return teacherSignUp(resource, token);
        }
        throw new InternalServerException();
    }

    private User studentSignUp(BsmUserResource resource, String oauthToken) {
        BsmStudent studentDto = resource.getStudent();
        Student student = studentFacade.find(
                studentDto.getEnrolledAt(),
                studentDto.getGrade(),
                studentDto.getClassNo(),
                studentDto.getStudentNo());

        User user = User.ofStudent(
                student,
                resource.getId(),
                resource.getNickname(),
                oauthToken);
        return userRepository.save(user);
    }

    private User teacherSignUp(BsmUserResource resource, String oauthToken) {
        Teacher teacher = Teacher.create(resource.getEmail(), resource.getTeacher().getName());
        teacher = teacherRepository.save(teacher);

        User user = User.ofTeacher(
                teacher,
                resource.getId(),
                resource.getNickname(),
                oauthToken);
        return userRepository.save(user);
    }

    private void studentUpdate(BsmUserResource resource, Student student) {
        BsmStudent studentDto = resource.getStudent();
        student.update(
                studentDto.getEnrolledAt(),
                studentDto.getGrade(),
                studentDto.getClassNo(),
                studentDto.getStudentNo());
    }

    private void teacherUpdate(BsmUserResource resource, Teacher teacher) {
        BsmTeacher teacherDto = resource.getTeacher();
        teacher.update(teacherDto.getName(), resource.getEmail());
    }

}
