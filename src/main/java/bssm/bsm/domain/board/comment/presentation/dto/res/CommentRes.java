package bssm.bsm.domain.board.comment.presentation.dto.res;

import bssm.bsm.domain.board.anonymous.service.AnonymousUserIdProvider;
import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.board.comment.domain.type.CommentAnonymousType;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRes {

    private long id;
    private UserRes user;
    private boolean isDelete;
    private String content;
    private LocalDateTime createdAt;
    private boolean permission;
    private boolean isAnonymous;
    private int depth;
    private List<CommentRes> child;

    public static CommentRes create(User nullableUser, Comment comment, AnonymousUserIdProvider anonymousUserIdProvider) {
        CommentRes res = new CommentRes();
        res.id = comment.getPk().getId();
        res.depth = comment.getDepth();
        if (comment.isDelete()) {
            res.setDeletedComment();
        } else {
            res.setNormalComment(nullableUser, comment, anonymousUserIdProvider);
        }

        if (!comment.getChildComments().isEmpty()) {
            res.child = comment.getChildComments().stream()
                    .map(childComment -> create(nullableUser, childComment, anonymousUserIdProvider))
                    .toList();
        }
        return res;
    }

    private void setNormalComment(User nullableUser, Comment comment, AnonymousUserIdProvider anonymousUserIdProvider) {
        this.user = UserRes.create(comment, anonymousUserIdProvider);
        this.isDelete = false;
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.permission = nullableUser != null && comment.hasPermission(nullableUser);
        this.isAnonymous = comment.getAnonymous() != CommentAnonymousType.VISIBLE;
    }

    private void setDeletedComment() {
        this.isDelete = true;
        this.permission = false;
        this.isAnonymous = false;
    }
}
