package bssm.bsm.board.post.dto;

import bssm.bsm.user.entities.User;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@SuperBuilder
public class PostDto {

    private int id;
    private User user;
    private String category;
    private String title;
    private Date createdAt;
    private int hit;
    private int totalComments;
    private int totalLikes;
}
