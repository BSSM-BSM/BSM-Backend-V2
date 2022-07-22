package bssm.bsm.user.service;

import bssm.bsm.user.UserService;
import bssm.bsm.user.dto.request.UserLoginDto;
import bssm.bsm.user.dto.request.UserSignUpDto;
import bssm.bsm.user.entities.Student;
import bssm.bsm.user.entities.User;
import bssm.bsm.user.repositories.StudentRepository;
import bssm.bsm.user.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Test
    @DisplayName("유저 회원 가입 테스트")
    void signUp() throws Exception {
        // Initialization
        studentRepository.findByAuthCode("A1B2C3").ifPresent(
                student -> {studentRepository.delete(student);}
        );
        userRepository.findById("testId").ifPresent(
                user -> {userRepository.delete(user);}
        );
        Student studentInfo = new Student("12345", true, "A1B2C3", 1, 2021, 1, 2, 3, "testName", "test@test.com");
        studentRepository.save(studentInfo);

        // Test
        UserSignUpDto dto = new UserSignUpDto("testId", "testPw", "testPw", "testNickname", "A1B2C3");
        User user = userService.signUp(dto);

        // Check
        userRepository.findById("testId").orElseThrow(
                () -> {
                    throw new IllegalStateException("유저 회원 가입 테스트 실패");
                }
        );
        studentRepository.findByAuthCode("A1B2C3").ifPresent(
                (student) -> {
                    if (student.isCodeAvailable()) throw new IllegalStateException("인증 코드 만료 테스트 실패");
                }
        );
    }

    @Test
    @DisplayName("유저 로그인 테스트")
    void login() throws Exception {
        // Initialization
        this.signUp();
        User user = userRepository.findById("testId").get();

        // Test
        UserLoginDto dto = new UserLoginDto("testId", "testPw");
        User testUser = userService.login(dto);

        // Check
        if (!user.getNickname().equals(testUser.getNickname())) {
            throw new IllegalStateException("유저 로그인 테스트 실패");
        }
    }
}
