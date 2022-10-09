package bssm.bsm.domain.board.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class WritePostRequest {

    @NotBlank
    private String category;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 100000)
    private String content;

    private boolean anonymous;
}
