package bssm.bsm.domain.board.like;

import bssm.bsm.domain.board.like.dto.request.LikeRequestDto;
import bssm.bsm.domain.board.like.dto.response.LikeResponseDto;
import bssm.bsm.domain.board.post.dto.request.PostIdDto;
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
    public LikeResponseDto viewPost(
            @PathVariable String boardId,
            @PathVariable int postId,
            @RequestBody LikeRequestDto dto
    ) {
        return likeService.like(userUtil.getCurrentUser(), new PostIdDto(boardId, postId), dto);
    }
}
