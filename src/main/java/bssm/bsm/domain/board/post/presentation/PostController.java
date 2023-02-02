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
    public PostListRes postList(
            @PathVariable String boardId,
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "l", defaultValue = "15") int limit,
            @RequestParam(value = "c", defaultValue = "all") String category,
            @RequestParam(value = "i", defaultValue = "-1") int startPostId
    ) {
        return postService.postList(userUtil.getOptionalUser(), new GetPostListReq(boardId, page, limit, category, startPostId));
    }

    @GetMapping("/{boardId}/{postId}")
    public DetailPostRes viewPost(@PathVariable String boardId, @PathVariable int postId) {
        return postService.viewPost(userUtil.getOptionalUser(), new PostReq(boardId, postId));
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
