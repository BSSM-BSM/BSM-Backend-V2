package bssm.bsm.board.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class WriteCommentDto {

    private int depth;
    private int parentId;
    private String content;
}
