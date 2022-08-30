package bssm.bsm.domain.board.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class WritePostDto {

    private String category;
    private String title;
    private String content;
    private boolean anonymous;
}
