package bssm.bsm.domain.user.presentation.dto.res;

import bssm.bsm.domain.board.anonymous.domain.AnonymousKeyType;
import bssm.bsm.domain.board.anonymous.service.AnonymousUserIdProvider;
import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.type.PostAnonymousType;
import bssm.bsm.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRes {

    private Long code;
    private String nickname;

    public static UserRes create(Post post) {
        UserRes userRes = new UserRes();
        if (post.getAnonymous() == PostAnonymousType.VISIBLE) {
            User user = post.getWriter();
            userRes.code = user.getCode();
            userRes.nickname = user.getNickname();
        } else {
            userRes.code = -1L;
            userRes.nickname = "ㅇㅇ";
        }
        return userRes;
    }

    public static UserRes create(Comment comment, AnonymousUserIdProvider anonymousUserIdProvider) {
        if (comment.isAnonymous()) return toAnonymousCommentUserRes(comment, anonymousUserIdProvider);

        User user = comment.getWriter();
        UserRes userRes = new UserRes();
        userRes.code = user.getCode();
        userRes.nickname = user.getNickname();
        return userRes;
    }

    private static UserRes toAnonymousCommentUserRes(Comment comment, AnonymousUserIdProvider anonymousUserIdProvider) {
        User user = comment.getWriter();
        Post post = comment.getPost();
        String sessionId = post.getBoard() + "/" + post.getPk().getId();
        long anonymousId = anonymousUserIdProvider.getAnonymousId(AnonymousKeyType.COMMENT, sessionId, user);

        UserRes userRes = new UserRes();
        userRes.code = -1L;
        userRes.nickname = "ㅇㅇ(" + anonymousId + ")";
        return userRes;
    }

    public UserRes(User user) {
        this.code = user.getCode();
        this.nickname = user.getNickname();
    }
}
