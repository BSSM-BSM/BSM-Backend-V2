package bssm.bsm.domain.user.repositories;

import bssm.bsm.domain.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {

    Optional<User> findByStudentId(String studentId);

    Optional<User> findByNickname(String nickname);
}
