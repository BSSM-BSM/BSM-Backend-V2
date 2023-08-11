package bssm.bsm.domain.board.comment.presentation.dto.res;

import bssm.bsm.domain.board.anonymous.service.AnonymousUserIdProvider;
import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRes {

    private long id;
    private UserRes user;
    private boolean isDelete;
    private String content;
    private Date createdAt;
    private boolean permission;
    private int depth;
    private List<CommentRes> child;

    public static CommentRes create(User nullableUser, Comment comment, AnonymousUserIdProvider anonymousUserIdProvider) {
        if (comment.isDelete()) {
            return createDeletedComment(nullableUser, comment, anonymousUserIdProvider);
        }
        return createNormalComment(nullableUser, comment, anonymousUserIdProvider);
    }

    public static CommentRes createNormalComment(User nullableUser, Comment comment, AnonymousUserIdProvider anonymousUserIdProvider) {
        CommentRes res = new CommentRes();
        res.id = comment.getPk().getId();
        res.user = UserRes.create(comment, anonymousUserIdProvider);
        res.isDelete = false;
        res.content = comment.getContent();
        res.createdAt = comment.getCreatedAt();
        res.permission = nullableUser != null && comment.checkPermission(nullableUser);
        res.depth = comment.getDepth();
        if (!comment.getChildComments().isEmpty()) {
            res.child = comment.getChildComments().stream()
                    .map(childComment -> create(nullableUser, childComment, anonymousUserIdProvider))
                    .toList();
        }
        return res;
    }

    public static CommentRes createDeletedComment(User nullableUser, Comment comment, AnonymousUserIdProvider anonymousUserIdProvider) {
        CommentRes res = new CommentRes();
        res.id = comment.getPk().getId();
        res.isDelete = true;
        res.depth = comment.getDepth();
        res.permission = false;
        if (!comment.getChildComments().isEmpty()) {
            res.child = comment.getChildComments().stream()
                    .map(childComment -> create(nullableUser, childComment, anonymousUserIdProvider))
                    .toList();
        }
        return res;
    }
}
