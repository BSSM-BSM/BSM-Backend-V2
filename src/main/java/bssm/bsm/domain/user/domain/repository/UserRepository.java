package bssm.bsm.domain.user.domain.repository;

import bssm.bsm.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> { }
