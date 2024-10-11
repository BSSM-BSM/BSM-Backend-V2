package bssm.bsm.domain.board.emoticon.presentation.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
public class EmoticonUploadReq {

    @Size(min = 2, max = 12)
    private String name;

    @Size(min = 2, max = 100)
    private String description;

    @NotNull
    private MultipartFile thumbnail;

    @Size(min = 4, max = 100)
    private List<MultipartFile> emoticonList;
}
