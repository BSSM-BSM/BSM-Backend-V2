package bssm.bsm.domain.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository <RefreshToken, String> {

    Optional<RefreshToken> findByTokenAndIsAvailable(String token, boolean isAvailable);
}