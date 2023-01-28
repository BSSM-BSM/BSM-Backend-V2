package bssm.bsm.domain.board.emoticon.presentation;

import bssm.bsm.domain.board.emoticon.presentation.dto.request.EmoticonUploadRequest;
import bssm.bsm.domain.board.emoticon.presentation.dto.response.EmoticonResponse;
import bssm.bsm.domain.board.emoticon.service.EmoticonService;
import bssm.bsm.global.auth.CurrentUser;
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
    private final CurrentUser userUtil;

    @GetMapping("inactive")
    public List<EmoticonResponse> getInactiveEmoticonList() {
        return emoticonService.getInactiveEmoticonList();
    }

    @GetMapping("{id}")
    public EmoticonResponse getEmoticon(@PathVariable long id) {
        return emoticonService.getEmoticon(id);
    }

    @GetMapping
    public List<EmoticonResponse> getEmoticonList() {
        return emoticonService.getEmoticonList();
    }

    @PostMapping
    public void upload(
            @RequestPart(value = "name") String name,
            @RequestPart(value = "description") String description,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "emoticonList") List<MultipartFile> emoticonList
    ) throws IOException {
        emoticonService.upload(userUtil.getUser(), new EmoticonUploadRequest(name, description, thumbnail, emoticonList));
    }

    @PutMapping("{id}")
    public void activeEmoticon(@PathVariable long id) {
        emoticonService.activeEmoticon(id);
    }

}
