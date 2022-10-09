package bssm.bsm.domain.board.post.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadFileResponse {

    private String id;
    private String fileExt;
}
