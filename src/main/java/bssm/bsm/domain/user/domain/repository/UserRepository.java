package bssm.bsm.domain.user.domain.repository;

import bssm.bsm.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {

    Optional<User> findByStudentId(String studentId);

    Optional<User> findByNickname(String nickname);
}
