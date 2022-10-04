package bssm.bsm.domain.board.emoticon;

import bssm.bsm.domain.board.emoticon.dto.request.EmoticonUploadRequestDto;
import bssm.bsm.domain.board.emoticon.dto.response.EmoticonResponseDto;
import bssm.bsm.global.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("emoticon")
@RequiredArgsConstructor
public class EmoticonController {

    private final EmoticonService emoticonService;
    private final UserUtil userUtil;

    @GetMapping("inactive")
    public List<EmoticonResponseDto> getInactiveEmoticonList() {
        return emoticonService.getInactiveEmoticonList();
    }

    @GetMapping("{id}")
    public EmoticonResponseDto getEmoticon(@PathVariable long id) {
        return emoticonService.getEmoticon(id);
    }

    @GetMapping
    public List<EmoticonResponseDto> getEmoticonList() {
        return emoticonService.getEmoticonList();
    }

    @PostMapping
    public void upload(
            @RequestPart(value = "name") String name,
            @RequestPart(value = "description") String description,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "emoticonList") List<MultipartFile> emoticonList
    ) throws IOException {
        emoticonService.upload(userUtil.getCurrentUser(), new EmoticonUploadRequestDto(name, description, thumbnail, emoticonList));
    }

    @PutMapping("{id}")
    public void activeEmoticon(@PathVariable long id) {
        emoticonService.activeEmoticon(id);
    }

}
