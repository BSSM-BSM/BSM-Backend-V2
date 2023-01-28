package bssm.bsm.domain.user.service;

import bssm.bsm.domain.board.anonymous.domain.AnonymousKeyType;
import bssm.bsm.domain.board.anonymous.service.AnonymousUserIdProvider;
import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.board.post.domain.PostPk;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserResProvider {

    private final AnonymousUserIdProvider anonymousUserIdProvider;

    public UserRes toBoardUserRes(User user, boolean anonymous) {
        if (anonymous) {
            return UserRes.builder()
                    .code((long) -1)
                    .nickname("ㅇㅇ")
                    .build();
        }
        return UserRes.builder()
                .code(user.getCode())
                .nickname(user.getNickname())
                .build();
    }

    public UserRes toCommentUserRes(Comment comment) {
        if (comment.isAnonymous()) return toAnonymousCommentUserRes(comment);

        User user = comment.getUser();
        return UserRes.builder()
                .code(user.getCode())
                .nickname(user.getNickname())
                .build();
    }

    private UserRes toAnonymousCommentUserRes(Comment comment) {
        User user = comment.getUser();
        PostPk postPk = comment.getPk().getPost().getPk();
        String sessionId = postPk.getBoard() + "/" + postPk.getId();

        long anonymousId = anonymousUserIdProvider.getAnonymousId(AnonymousKeyType.COMMENT, sessionId, user);

        return UserRes.builder()
                .code(-1L)
                .nickname("ㅇㅇ(" + anonymousId + ")")
                .build();
    }

}
