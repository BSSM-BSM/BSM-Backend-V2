package bssm.bsm.board.post;

import bssm.bsm.board.post.dto.request.ViewPostDto;
import bssm.bsm.board.post.dto.request.WritePostDto;
import bssm.bsm.board.post.dto.response.ViewPostResponseDto;
import bssm.bsm.global.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final UserUtil userUtil;
    private final PostService postService;

    @GetMapping("/{boardId}/{postId}")
    public ViewPostResponseDto viewPost(@PathVariable String boardId, @PathVariable int postId) {
        return postService.viewPost(userUtil.getCurrentUser(), new ViewPostDto(boardId, postId));
    }

    @PostMapping("/{boardId}")
    public void writePost(@RequestBody WritePostDto dto) {
        postService.writePost(userUtil.getCurrentUser(), dto);
    }
}
