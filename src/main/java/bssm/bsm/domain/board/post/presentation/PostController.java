package bssm.bsm.domain.board.post.presentation;

import bssm.bsm.domain.board.post.presentation.dto.request.GetPostListRequest;
import bssm.bsm.domain.board.post.presentation.dto.request.PostIdRequest;
import bssm.bsm.domain.board.post.presentation.dto.request.WritePostRequest;
import bssm.bsm.domain.board.post.presentation.dto.response.UploadFileResponse;
import bssm.bsm.domain.board.post.presentation.dto.response.PostListResponse;
import bssm.bsm.domain.board.post.presentation.dto.response.ViewPostResponse;
import bssm.bsm.domain.board.post.service.PostService;
import bssm.bsm.global.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final UserUtil userUtil;
    private final PostService postService;

    @GetMapping("/{boardId}")
    public PostListResponse postList(
            @PathVariable String boardId,
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "l", defaultValue = "15") int limit,
            @RequestParam(value = "c", defaultValue = "all") String category,
            @RequestParam(value = "i", defaultValue = "-1") int startPostId
    ) {
        return postService.postList(userUtil.getOptionalUser(), boardId, new GetPostListRequest(page, limit, category, startPostId));
    }

    @GetMapping("/{boardId}/{postId}")
    public ViewPostResponse viewPost(@PathVariable String boardId, @PathVariable int postId) {
        return postService.viewPost(userUtil.getOptionalUser(), new PostIdRequest(boardId, postId));
    }

    @PostMapping("/{boardId}")
    public long writePost(@PathVariable String boardId, @Valid @RequestBody WritePostRequest dto) {
        return postService.writePost(userUtil.getUser(), boardId, dto);
    }

    @PutMapping("/{boardId}/{postId}")
    public void modifyPost(
            @PathVariable String boardId,
            @PathVariable int postId,
            @RequestBody WritePostRequest dto
    ) {
        postService.modifyPost(userUtil.getUser(), new PostIdRequest(boardId, postId), dto);
    }

    @DeleteMapping("/{boardId}/{postId}")
    public void deletePost(@PathVariable String boardId, @PathVariable int postId) {
        postService.deletePost(userUtil.getUser(), new PostIdRequest(boardId, postId));
    }

    @PostMapping("upload")
    public UploadFileResponse uploadFile(@RequestParam MultipartFile file) {
        return postService.uploadFile(file);
    }
}