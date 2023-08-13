package bssm.bsm.domain.board.like.presentation.dto.res;

import bssm.bsm.domain.board.like.domain.enums.Like;
import bssm.bsm.domain.board.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeRes {

    private int like;
    private int totalLikes;

    public static LikeRes create(Like like, Post post) {
        LikeRes likeRes = new LikeRes();
        likeRes.like = like.getValue();
        likeRes.totalLikes = post.getTotalLikes();
        return likeRes;
    }
}
