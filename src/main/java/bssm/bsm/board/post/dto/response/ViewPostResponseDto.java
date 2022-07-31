package bssm.bsm.board.post.dto.response;

import bssm.bsm.board.post.dto.PostDto;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ViewPostResponseDto extends PostDto {

    private String content;
    private boolean permission;
    private boolean like;
}
