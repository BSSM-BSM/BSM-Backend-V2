package bssm.bsm.domain.board.post.presentation.dto.res;

import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostRes {

    protected long id;
    protected UserRes user;
    protected String category;
    protected String title;
    protected LocalDateTime createdAt;
    protected int view;
    protected int totalComments;
    protected int totalLikes;

    public static PostRes create(Post post) {
        PostRes postRes = new PostRes();
        postRes.id = post.getId();
        postRes.user = UserRes.create(post);
        postRes.category =  post.getCategoryId();
        postRes.title = post.getTitle();
        postRes.createdAt = post.getCreatedAt();
        postRes.view = post.getViewCount();
        postRes.totalComments = post.getCommentCount();
        postRes.totalLikes = post.getLikeCount();
        return postRes;
    }
}
