package bssm.bsm.domain.board.post.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadFileResponseDto {

    private String id;
    private String fileExt;
}
