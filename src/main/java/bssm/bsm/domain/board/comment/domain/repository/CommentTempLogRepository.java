package bssm.bsm.domain.board.comment.domain.repository;

import bssm.bsm.domain.board.comment.domain.CommentTempLog;
import org.springframework.data.repository.CrudRepository;

public interface CommentTempLogRepository extends CrudRepository<CommentTempLog, String> {}