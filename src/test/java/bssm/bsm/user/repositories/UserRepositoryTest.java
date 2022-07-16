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
    private UserRepository repository;

    @Test
    @DisplayName("유저 DB 저장 확인")
    void save() {
        User user = new User();
        user.setId("testId");
        user.setPw("testPw");
        user.setPwSalt("testPwSalt");
        user.setNickname("testNickname");
        user.setLevel(0);
        user.setCreatedAt(new Date());
        user.setUniqNo("0");

        repository.save(user);

        User result = repository.findById("testId").get();
        Assertions.assertThat(result.getNickname()).isEqualTo("testNickname");
    }
}
