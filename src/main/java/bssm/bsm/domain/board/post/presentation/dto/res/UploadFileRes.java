package bssm.bsm.domain.board.post.presentation.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UploadFileRes {

    private String id;
    private String fileExt;

    public static UploadFileRes create(String id, String fileExt) {
        UploadFileRes uploadFileRes = new UploadFileRes();
        uploadFileRes.id = id;
        uploadFileRes.fileExt = fileExt;
        return uploadFileRes;
    }
}
