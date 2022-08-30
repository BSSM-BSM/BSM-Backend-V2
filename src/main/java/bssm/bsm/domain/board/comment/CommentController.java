package bssm.bsm.domain.board.comment;

import bssm.bsm.domain.board.comment.dto.request.WriteCommentDto;
import bssm.bsm.domain.board.comment.dto.response.CommentDto;
import bssm.bsm.domain.board.post.dto.request.PostIdDto;
import bssm.bsm.global.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final UserUtil userUtil;
    private final CommentService commentService;

    @PostMapping("/{boardId}/{postId}")
    public void writeComment(
            @PathVariable String boardId,
            @PathVariable int postId,
            @RequestBody WriteCommentDto dto
    ) {
        commentService.writeComment(userUtil.getCurrentUser(), new PostIdDto(boardId, postId), dto);
    }

    @DeleteMapping("/{boardId}/{postId}/{commentId}")
    public void deleteComment(
            @PathVariable String boardId,
            @PathVariable int postId,
            @PathVariable int commentId
    ) {
        commentService.deleteComment(userUtil.getCurrentUser(), new PostIdDto(boardId, postId), commentId);
    }

    @GetMapping("/{boardId}/{postId}")
    public List<CommentDto> viewComment(
            @PathVariable String boardId,
            @PathVariable int postId
    ) {
        return commentService.viewCommentList(userUtil.getCurrentUser(), new PostIdDto(boardId, postId));
    }
}