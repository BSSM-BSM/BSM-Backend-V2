package bssm.bsm.domain.board.like.presentation;

import bssm.bsm.domain.board.like.presentation.dto.request.LikeRequest;
import bssm.bsm.domain.board.like.presentation.dto.response.LikeResponse;
import bssm.bsm.domain.board.like.service.LikeService;
import bssm.bsm.domain.board.post.presentation.dto.request.PostIdRequest;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("like")
@RequiredArgsConstructor
public class LikeController {

    private final CurrentUser userUtil;
    private final LikeService likeService;

    @PostMapping("/{boardId}/{postId}")
    public LikeResponse like(
            @PathVariable String boardId,
            @PathVariable int postId,
            @RequestBody LikeRequest dto
    ) {
        return likeService.like(userUtil.getUser(), new PostIdRequest(boardId, postId), dto);
    }
}
