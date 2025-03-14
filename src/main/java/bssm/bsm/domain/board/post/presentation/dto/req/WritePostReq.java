package bssm.bsm.domain.board.post.presentation.dto.req;

import bssm.bsm.domain.board.post.domain.type.PostAnonymousType;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    @NotNull
    private PostAnonymousType anonymous;
}
