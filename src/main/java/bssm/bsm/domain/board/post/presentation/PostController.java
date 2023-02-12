package bssm.bsm.domain.board.post.presentation;

import bssm.bsm.domain.board.post.presentation.dto.req.*;
import bssm.bsm.domain.board.post.presentation.dto.res.PostListRes;
import bssm.bsm.domain.board.post.presentation.dto.res.DetailPostRes;
import bssm.bsm.domain.board.post.service.PostService;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final CurrentUser currentUser;
    private final PostService postService;

    @GetMapping("/{boardId}")
    public PostListRes findPostList(
            @PathVariable String boardId,
            @RequestParam(value = "limit", defaultValue = "15") int limit,
            @RequestParam(value = "category", defaultValue = "all") String category,
            @RequestParam(value = "startPostId") long startPostId
    ) {
        return postService.findPostList(currentUser.getUserOrNull(), new FindPostListReq(boardId, limit, category, startPostId));
    }

    @GetMapping("/{boardId}/recent")
    public PostListRes findRecentPostList(
            @PathVariable String boardId,
            @RequestParam(value = "limit", defaultValue = "15") int limit,
            @RequestParam(value = "category", defaultValue = "all") String category
    ) {
        return postService.findRecentPostList(currentUser.getUserOrNull(), new FindRecentPostListReq(boardId, limit, category));
    }

    @GetMapping("/{boardId}/{postId}")
    public DetailPostRes findPost(@PathVariable String boardId, @PathVariable int postId) {
        return postService.findPost(currentUser.getUserOrNull(), new PostReq(boardId, postId));
    }

    @PostMapping
    public long createPost(@Valid @RequestBody WritePostReq req) {
        return postService.createPost(currentUser.getUser(), req);
    }

    @PutMapping
    public void updatePost(@RequestBody UpdatePostReq req) {
        postService.updatePost(currentUser.getUser(), req);
    }

    @DeleteMapping("/{boardId}/{postId}")
    public void deletePost(@PathVariable String boardId, @PathVariable int postId) {
        postService.deletePost(currentUser.getUser(), new PostReq(boardId, postId));
    }
}
