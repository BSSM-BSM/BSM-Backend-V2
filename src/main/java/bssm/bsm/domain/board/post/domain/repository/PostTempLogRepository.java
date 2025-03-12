package bssm.bsm.domain.board.post.domain.repository;

import bssm.bsm.domain.board.post.domain.PostTempLog;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface PostTempLogRepository extends KeyValueRepository<PostTempLog, String> {}