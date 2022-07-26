package bssm.bsm.user.repositories;

import bssm.bsm.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Integer> {

    Optional<User> findById(String userId);

    Optional<User> findByNickname(String nickname);
}
