package bssm.bsm.domain.board.post.service;

import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.PostTempLog;
import bssm.bsm.domain.board.post.domain.repository.PostTempLogRepository;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLogService {

    private final PostTempLogRepository postTempLogRepository;

    public void recordTempLog(Post post, User writer) {
        PostTempLog log = PostTempLog.create(post, writer);
        postTempLogRepository.save(log);
    }

}
