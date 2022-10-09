package bssm.bsm.domain.board.like;

import bssm.bsm.domain.board.like.dto.request.LikeRequest;
import bssm.bsm.domain.board.like.dto.response.LikeResponse;
import bssm.bsm.domain.board.post.dto.request.PostIdRequest;
import bssm.bsm.global.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("like")
@RequiredArgsConstructor
public class LikeController {

    private final UserUtil userUtil;
    private final LikeService likeService;

    @PostMapping("/{boardId}/{postId}")
    public LikeResponse viewPost(
            @PathVariable String boardId,
            @PathVariable int postId,
            @RequestBody LikeRequest dto
    ) {
        return likeService.like(userUtil.getCurrentUser(), new PostIdRequest(boardId, postId), dto);
    }
}
