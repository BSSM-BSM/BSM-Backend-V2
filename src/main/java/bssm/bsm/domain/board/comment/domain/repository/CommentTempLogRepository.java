package bssm.bsm.domain.board.comment.domain.repository;

import bssm.bsm.domain.board.comment.domain.CommentTempLog;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface CommentTempLogRepository extends KeyValueRepository<CommentTempLog, String> {}