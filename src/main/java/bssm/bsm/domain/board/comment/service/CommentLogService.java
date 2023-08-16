package bssm.bsm.domain.board.comment.service;

import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.board.comment.domain.CommentTempLog;
import bssm.bsm.domain.board.comment.domain.repository.CommentTempLogRepository;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLogService {

    private final CommentTempLogRepository commentTempLogRepository;

    public void recordTempLog(Comment comment, User writer) {
        CommentTempLog log = CommentTempLog.create(comment, writer);
        commentTempLogRepository.save(log);
    }

}
