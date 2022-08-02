package bssm.bsm.board.comment;

import bssm.bsm.board.comment.dto.request.WriteCommentDto;
import bssm.bsm.board.post.dto.request.PostIdDto;
import bssm.bsm.global.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}