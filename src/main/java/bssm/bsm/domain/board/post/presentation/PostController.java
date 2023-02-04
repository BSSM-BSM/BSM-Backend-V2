package bssm.bsm.domain.board.post.presentation;

import bssm.bsm.domain.board.post.presentation.dto.req.GetPostListReq;
import bssm.bsm.domain.board.post.presentation.dto.req.PostReq;
import bssm.bsm.domain.board.post.presentation.dto.req.UpdatePostReq;
import bssm.bsm.domain.board.post.presentation.dto.req.WritePostReq;
import bssm.bsm.domain.board.post.presentation.dto.res.UploadFileRes;
import bssm.bsm.domain.board.post.presentation.dto.res.PostListRes;
import bssm.bsm.domain.board.post.presentation.dto.res.DetailPostRes;
import bssm.bsm.domain.board.post.service.PostService;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final CurrentUser userUtil;
    private final PostService postService;

    @GetMapping("/{boardId}")
    public PostListRes findPostList(
            @PathVariable String boardId,
            @RequestParam(value = "limit", defaultValue = "15") int limit,
            @RequestParam(value = "category", defaultValue = "all") String category,
            @RequestParam(value = "postId", defaultValue = "-1") int postId
    ) {
        return postService.findPostList(userUtil.getOptionalUser(), new GetPostListReq(boardId, limit, category, postId));
    }

    @GetMapping("/{boardId}/{postId}")
    public DetailPostRes findPost(@PathVariable String boardId, @PathVariable int postId) {
        return postService.findPost(userUtil.getOptionalUser(), new PostReq(boardId, postId));
    }

    @PostMapping
    public long createPost(@Valid @RequestBody WritePostReq req) {
        return postService.createPost(userUtil.getUser(), req);
    }

    @PutMapping
    public void updatePost(@RequestBody UpdatePostReq req) {
        postService.updatePost(userUtil.getUser(), req);
    }

    @DeleteMapping("/{boardId}/{postId}")
    public void deletePost(@PathVariable String boardId, @PathVariable int postId) {
        postService.deletePost(userUtil.getUser(), new PostReq(boardId, postId));
    }
}
