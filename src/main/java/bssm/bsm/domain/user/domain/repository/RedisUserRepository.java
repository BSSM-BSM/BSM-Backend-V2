package bssm.bsm.domain.user.domain.repository;

import bssm.bsm.domain.user.domain.UserCache;
import org.springframework.data.repository.CrudRepository;

public interface RedisUserRepository extends CrudRepository<UserCache, Long> {}
