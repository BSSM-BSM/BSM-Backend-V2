package bssm.bsm.user.repositories;

import bssm.bsm.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long>{

    Optional<User> findById(String userId);

    Optional<User> findByNickname(String nickname);
}
