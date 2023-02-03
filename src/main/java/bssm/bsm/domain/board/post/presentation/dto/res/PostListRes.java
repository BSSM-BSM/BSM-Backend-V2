package bssm.bsm.domain.board.post.presentation.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostListRes {

    List<PostRes> postList;
    int limit;

    public static PostListRes create(List<PostRes> postList, int limit) {
        PostListRes postListRes = new PostListRes();
        postListRes.postList = postList;
        postListRes.limit = limit;
        return postListRes;
    }
}
