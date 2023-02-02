package bssm.bsm.domain.board.comment.presentation.dto.res;

import bssm.bsm.domain.user.presentation.dto.res.UserRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

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
}
