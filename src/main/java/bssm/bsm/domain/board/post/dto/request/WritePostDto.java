package bssm.bsm.domain.board.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class WritePostDto {

    private String category;
    @NotBlank
    @Size(max = 50)
    private String title;
    @NotBlank
    @Size(max = 100000)
    private String content;
    private boolean anonymous;
}
