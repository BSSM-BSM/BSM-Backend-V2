package bssm.bsm.domain.board.post.presentation.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ViewPostResponse extends PostResponse {

    private String content;
    private boolean permission;
    private int like;
    private boolean anonymous;
}
