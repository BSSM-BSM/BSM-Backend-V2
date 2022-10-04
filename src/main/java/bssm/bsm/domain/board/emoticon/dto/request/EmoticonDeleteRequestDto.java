package bssm.bsm.domain.board.emoticon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class EmoticonDeleteRequestDto {

    private String msg;

}
