package bssm.bsm.domain.board.comment.dto.response;

import bssm.bsm.domain.user.dto.response.UserResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

    private long id;
    private UserResponseDto user;
    private boolean isDelete;
    private String content;
    private Date createdAt;
    private boolean permission;
    private int depth;
    private List<CommentDto> child;
}
