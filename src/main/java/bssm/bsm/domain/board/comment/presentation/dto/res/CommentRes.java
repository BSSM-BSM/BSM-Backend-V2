package bssm.bsm.domain.board.comment.presentation.dto.res;

import bssm.bsm.domain.board.anonymous.service.AnonymousUserIdProvider;
import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
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

    public void setChild(List<CommentRes> child) {
        this.child = child;
    }

    public static CommentRes create(Optional<User> user, Comment comment, AnonymousUserIdProvider anonymousUserIdProvider) {
        if (comment.isDelete()) {
            return CommentRes.builder()
                    .id(comment.getPk().getId())
                    .isDelete(true)
                    .depth(comment.getDepth())
                    .permission(false)
                    .build();
        }
        return CommentRes.builder()
                .id(comment.getPk().getId())
                .isDelete(false)
                .user(UserRes.create(comment, anonymousUserIdProvider))
                .createdAt(comment.getCreatedAt())
                .content(comment.getContent())
                .depth(comment.getDepth())
                .permission(user.isPresent() && comment.checkPermission(user.get()))
                .build();
    }
}
