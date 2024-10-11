package bssm.bsm.domain.board.comment.presentation;

import bssm.bsm.domain.board.comment.presentation.dto.req.DeleteCommentReq;
import bssm.bsm.domain.board.comment.presentation.dto.req.FindCommentTreeReq;
import bssm.bsm.domain.board.comment.presentation.dto.req.UpdateNoRecordCommentReq;
import bssm.bsm.domain.board.comment.service.CommentService;
import bssm.bsm.domain.board.comment.presentation.dto.req.WriteCommentReq;
import bssm.bsm.domain.board.comment.presentation.dto.res.CommentRes;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final CurrentUser currentUser;
    private final CommentService commentService;

    @PostMapping
    public void writeComment(@Valid @RequestBody WriteCommentReq req) {
        commentService.writeComment(currentUser.getUser(), req);
    }

    @DeleteMapping("/{boardId}/{postId}/{commentId}")
    public void deleteComment(
            @PathVariable String boardId,
            @PathVariable long postId,
            @PathVariable int commentId
    ) {
        commentService.deleteComment(currentUser.getUser(), new DeleteCommentReq(boardId, postId, commentId));
    }

    @PutMapping("/{boardId}/{postId}/{commentId}/no-record")
    public void updateCommentNoRecord(
            @PathVariable String boardId,
            @PathVariable long postId,
            @PathVariable int commentId
    ) {
        commentService.updateCommentNoRecord(currentUser.getUser(), new UpdateNoRecordCommentReq(boardId, postId, commentId));
    }

    @GetMapping("/{boardId}/{postId}")
    public List<CommentRes> viewCommentTree(
            @PathVariable String boardId,
            @PathVariable int postId
    ) {
        return commentService.viewCommentTree(currentUser.getUserOrNull(), new FindCommentTreeReq(boardId, postId));
    }
}