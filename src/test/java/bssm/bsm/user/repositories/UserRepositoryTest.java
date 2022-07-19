package bssm.bsm.user.repositories;

import bssm.bsm.user.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 DB 저장 확인")
    void save() {
        User user = User.builder()
                        .id("testId")
                        .pw("testPw")
                        .pwSalt("testPwSalt")
                        .nickname("testNickname")
                        .level(0)
                        .createdAt(new Date())
                        .uniqNo("0")
                        .build();

        userRepository.save(user);

        User result = userRepository.findById("testId").get();
        Assertions.assertThat(result.getNickname()).isEqualTo("testNickname");

        // Test clear
        userRepository.delete(user);
    }
}
