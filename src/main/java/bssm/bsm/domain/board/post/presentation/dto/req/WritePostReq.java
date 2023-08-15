package bssm.bsm.domain.board.post.presentation.dto.req;

import bssm.bsm.domain.board.post.domain.type.PostAnonymousType;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class WritePostReq {

    @NotBlank
    private String boardId;

    @NotBlank
    private String categoryId;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 100000)
    private String content;

    private PostAnonymousType anonymous;
}
