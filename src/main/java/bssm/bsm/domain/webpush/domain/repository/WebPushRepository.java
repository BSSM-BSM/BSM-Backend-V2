package bssm.bsm.domain.webpush.domain.repository;

import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.webpush.domain.WebPush;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebPushRepository extends JpaRepository<WebPush, String> {

    Optional<WebPush> findByUserIdAndEndpoint(long userId, String endpoint);

    List<WebPush> findAllByUserId(long userId);

    List<WebPush> findAllByUserIn(List<User> userList);

}
