package bssm.bsm.domain.board.post.presentation.dto.response;

import bssm.bsm.domain.user.presentation.dto.response.UserResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@SuperBuilder
public class PostResponse {

    private long id;
    private UserResponse user;
    private String category;
    private String title;
    private Date createdAt;
    private int hit;
    private int totalComments;
    private int totalLikes;
}
