package bssm.bsm.domain.board.post.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadFileRes {

    private String id;
    private String fileExt;
}
