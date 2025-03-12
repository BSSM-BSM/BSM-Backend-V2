package bssm.bsm.domain.board.post.presentation.dto.res;

import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.type.PostAnonymousType;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailPostRes extends PostRes {

    private String content;
    private boolean permission;
    private int myLike;
    private boolean isAnonymous;

    public static DetailPostRes create(Post post, PostLike postLike, User nullableViewer) {
        DetailPostRes detailPostRes = new DetailPostRes();
        detailPostRes.id = post.getId();
        detailPostRes.user = UserRes.create(post);
        detailPostRes.category =  post.getCategoryId();
        detailPostRes.title = post.getTitle();
        detailPostRes.createdAt = post.getCreatedAt();
        detailPostRes.view = post.getViewCount();
        detailPostRes.totalComments = post.getCommentCount();
        detailPostRes.totalLikes = post.getLikeCount();
        detailPostRes.content = post.getContent();
        detailPostRes.permission = nullableViewer != null && post.hasPermission(nullableViewer);
        detailPostRes.myLike = postLike == null ? 0 : postLike.getType().getValue();
        detailPostRes.isAnonymous = post.getAnonymousType() != PostAnonymousType.VISIBLE;
        return detailPostRes;
    }
}
