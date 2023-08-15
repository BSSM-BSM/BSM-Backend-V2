package bssm.bsm.domain.board.post.domain.repository;

import bssm.bsm.domain.board.post.domain.PostPk;
import bssm.bsm.domain.board.post.domain.PostTempLog;
import org.springframework.data.repository.CrudRepository;

public interface PostTempLogRepository extends CrudRepository<PostTempLog, PostPk> {}