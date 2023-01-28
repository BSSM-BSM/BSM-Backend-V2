package bssm.bsm.domain.board.comment.presentation;

import bssm.bsm.domain.board.comment.service.CommentService;
import bssm.bsm.domain.board.comment.presentation.dto.request.WriteCommentRequest;
import bssm.bsm.domain.board.comment.presentation.dto.response.CommentResponse;
import bssm.bsm.domain.board.post.presentation.dto.request.PostIdRequest;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final CurrentUser userUtil;
    private final CommentService commentService;

    @PostMapping("/{boardId}/{postId}")
    public void writeComment(
            @PathVariable String boardId,
            @PathVariable int postId,
            @Valid @RequestBody WriteCommentRequest dto
    ) {
        commentService.writeComment(userUtil.getUser(), new PostIdRequest(boardId, postId), dto);
    }

    @DeleteMapping("/{boardId}/{postId}/{commentId}")
    public void deleteComment(
            @PathVariable String boardId,
            @PathVariable int postId,
            @PathVariable int commentId
    ) {
        commentService.deleteComment(userUtil.getUser(), new PostIdRequest(boardId, postId), commentId);
    }

    @GetMapping("/{boardId}/{postId}")
    public List<CommentResponse> viewComment(
            @PathVariable String boardId,
            @PathVariable int postId
    ) {
        return commentService.viewCommentList(userUtil.getOptionalUser(), new PostIdRequest(boardId, postId));
    }
}