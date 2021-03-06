package bssm.bsm.board.post;

import bssm.bsm.board.post.dto.request.GetPostListDto;
import bssm.bsm.board.post.dto.request.ModifyPostDto;
import bssm.bsm.board.post.dto.request.PostIdDto;
import bssm.bsm.board.post.dto.request.WritePostDto;
import bssm.bsm.board.post.dto.response.PostListResponseDto;
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

    @GetMapping("/{boardId}")
    public PostListResponseDto postList(
            @PathVariable String boardId,
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "l", defaultValue = "15") int limit,
            @RequestParam(value = "c", defaultValue = "all") String category,
            @RequestParam(value = "i", defaultValue = "-1") int startPostId
    ) {
        return postService.postList(boardId, new GetPostListDto(page, limit, category, startPostId));
    }

    @GetMapping("/{boardId}/{postId}")
    public ViewPostResponseDto viewPost(@PathVariable String boardId, @PathVariable int postId) {
        return postService.viewPost(userUtil.getCurrentUser(), new PostIdDto(boardId, postId));
    }

    @PostMapping("/{boardId}")
    public void writePost(@PathVariable String boardId, @RequestBody WritePostDto dto) {
        postService.writePost(userUtil.getCurrentUser(), boardId, dto);
    }

    @PutMapping("/{boardId}/{postId}")
    public void modifyPost(
            @PathVariable String boardId,
            @PathVariable int postId,
            @RequestBody ModifyPostDto dto
    ) {
        postService.modifyPost(userUtil.getCurrentUser(), new PostIdDto(boardId, postId), dto);
    }

    @DeleteMapping("/{boardId}/{postId}")
    public void deletePost(@PathVariable String boardId, @PathVariable int postId) {
        postService.deletePost(userUtil.getCurrentUser(), new PostIdDto(boardId, postId));
    }
}
