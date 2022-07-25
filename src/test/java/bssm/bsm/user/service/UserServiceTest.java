package bssm.bsm.user.service;

import bssm.bsm.user.UserService;
import bssm.bsm.user.dto.request.UserLoginDto;
import bssm.bsm.user.dto.request.UserSignUpDto;
import bssm.bsm.user.dto.request.UserUpdateNicknameDto;
import bssm.bsm.user.dto.request.UserUpdatePwDto;
import bssm.bsm.user.entities.Student;
import bssm.bsm.user.entities.User;
import bssm.bsm.user.repositories.StudentRepository;
import bssm.bsm.user.repositories.UserRepository;
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
        userRepository.findById("testId").ifPresent(
                user -> {userRepository.delete(user);}
        );
        studentRepository.findByAuthCode("A1B2C3").ifPresent(
                student -> {studentRepository.delete(student);}
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

    @Test
    @DisplayName("유저 비밀번호 변경 테스트")
    void updatePw() throws Exception {
        // Initialization
        this.signUp();
        User user = userRepository.findById("testId").get();

        // Test
        UserUpdatePwDto invalidDto = new UserUpdatePwDto("newPw", "newPw1");
        try {
            userService.updatePw(user, invalidDto);
            throw new IllegalStateException("비밀번호 재입력 테스트 실패");
        } catch (Exception ignored) {}
        UserUpdatePwDto validDto = new UserUpdatePwDto("newPw", "newPw");
        userService.updatePw(user, validDto);

        // Check
        UserLoginDto dto = new UserLoginDto("testId", "newPw");
        userService.login(dto);
    }

    @Test
    @DisplayName("유저 닉네임 변경 테스트")
    void updateNickname() throws Exception {
        // Initialization
        this.signUp();
        User user = userRepository.findById("testId").get();

        // Test
        UserUpdateNicknameDto dto = new UserUpdateNicknameDto("newNickname");
        User newUser = userService.updateNickname(user, dto);

        // Check
        if (!newUser.getNickname().equals("newNickname")) {
            throw new IllegalStateException("유저 닉네임 변경 테스트 실패");
        }
    }
}
