package bssm.bsm.domain.board.comment.presentation.dto.response;

import bssm.bsm.domain.user.presentation.dto.response.UserResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {

    private long id;
    private UserResponse user;
    private boolean isDelete;
    private String content;
    private Date createdAt;
    private boolean permission;
    private int depth;
    private List<CommentResponse> child;

    public void setChild(List<CommentResponse> child) {
        this.child = child;
    }
}
