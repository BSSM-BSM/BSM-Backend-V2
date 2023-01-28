package bssm.bsm.domain.user.service;

import bssm.bsm.domain.board.anonymous.domain.AnonymousKeyType;
import bssm.bsm.domain.board.anonymous.service.AnonymousProvider;
import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserResProvider {

    private final AnonymousProvider anonymousProvider;

    public UserRes toBoardUserResponse(User user, boolean anonymous) {
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

    public UserRes toCommentUserResponse(Comment comment) {
        User user = comment.getUser();
        long postId = comment.getPk().getPost().getPk().getId();

        if (comment.isAnonymous()) {
            return UserRes.builder()
                    .code(-1L)
                    .nickname("ㅇㅇ(" + anonymousProvider.getAnonymousIdx(AnonymousKeyType.COMMENT, postId, user) + ")")
                    .build();
        }
        return UserRes.builder()
                .code(user.getCode())
                .nickname(user.getNickname())
                .build();
    }

}
