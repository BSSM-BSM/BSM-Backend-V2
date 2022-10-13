package bssm.bsm.domain.webpush.domain.repository;

import bssm.bsm.domain.user.entities.User;
import bssm.bsm.domain.webpush.domain.WebPush;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebPushRepository extends JpaRepository<WebPush, String> {

    Optional<WebPush> findByUserCodeAndEndpoint(long userCode, String endpoint);

    List<WebPush> findAllByUserCode(long userCode);

    List<WebPush> findAllByUserIn(List<User> userList);

}
