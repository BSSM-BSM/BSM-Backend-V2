package bssm.bsm.domain.board.post.presentation.dto.res;

import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@NoArgsConstructor
public class DetailPostRes extends PostRes {

    private String content;
    private boolean permission;
    private int myLike;
    private boolean anonymous;

    public static DetailPostRes create(Post post, PostLike postLike, Optional<User> viewer) {
        DetailPostRes detailPostRes = new DetailPostRes();
        detailPostRes.id = post.getPk().getId();
        detailPostRes.user = UserRes.create(post);
        detailPostRes.category =  post.getCategoryId();
        detailPostRes.title = post.getTitle();
        detailPostRes.createdAt = post.getCreatedAt();
        detailPostRes.view = post.getView();
        detailPostRes.totalComments = post.getTotalComments();
        detailPostRes.totalLikes = post.getTotalLikes();
        detailPostRes.content = post.getContent();
        detailPostRes.permission = viewer.isPresent() && post.checkPermission(viewer.get());
        detailPostRes.myLike = postLike.getLike();
        detailPostRes.anonymous = post.isAnonymous();
        return detailPostRes;
    }
}
