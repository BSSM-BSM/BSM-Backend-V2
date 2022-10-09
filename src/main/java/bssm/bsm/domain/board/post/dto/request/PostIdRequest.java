package bssm.bsm.domain.board.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class PostIdRequest {

    @NotBlank
    private String board;

    @Positive
    private int postId;
}
