package bssm.bsm.board.post.dto.response;

import bssm.bsm.user.entities.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ViewPostResponseDto {

    private User user;
    private String title;
    private String content;
    private Date createdAt;
    private int hit;
    private int totalComments;
    private int totalLikes;
    private boolean permission;
    private boolean like;
}
