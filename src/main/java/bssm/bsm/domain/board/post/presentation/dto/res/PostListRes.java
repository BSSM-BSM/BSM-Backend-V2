package bssm.bsm.domain.board.post.presentation.dto.res;

import bssm.bsm.domain.board.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostListRes {

    List<PostRes> postList;
    int limit;

    public static PostListRes create(List<Post> postList, int limit) {
        List<PostRes> postResList = postList.stream()
                .map(PostRes::create)
                .toList();

        PostListRes postListRes = new PostListRes();
        postListRes.postList = postResList;
        postListRes.limit = limit;
        return postListRes;
    }
}
