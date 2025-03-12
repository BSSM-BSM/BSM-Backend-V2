package bssm.bsm.domain.user.domain.repository;

import bssm.bsm.domain.user.domain.UserCache;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface RedisUserRepository extends KeyValueRepository<UserCache, Long> {}
