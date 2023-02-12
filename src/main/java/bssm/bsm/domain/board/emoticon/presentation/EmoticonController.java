package bssm.bsm.domain.board.emoticon.presentation;

import bssm.bsm.domain.board.emoticon.presentation.dto.req.EmoticonUploadReq;
import bssm.bsm.domain.board.emoticon.presentation.dto.res.EmoticonRes;
import bssm.bsm.domain.board.emoticon.service.EmoticonService;
import bssm.bsm.domain.board.emoticon.service.EmoticonUploadService;
import bssm.bsm.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("emoticon")
@RequiredArgsConstructor
public class EmoticonController {

    private final EmoticonService emoticonService;
    private final EmoticonUploadService emoticonUploadService;
    private final CurrentUser currentUser;

    @GetMapping("{id}")
    public EmoticonRes getEmoticon(@PathVariable long id) {
        return emoticonService.getEmoticon(id);
    }

    @GetMapping
    public List<EmoticonRes> getEmoticonList() {
        return emoticonService.getEmoticonList();
    }

    @PostMapping
    public void upload(
            @RequestPart(value = "name") String name,
            @RequestPart(value = "description") String description,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "emoticonList") List<MultipartFile> emoticonList
    ) {
        emoticonUploadService.upload(currentUser.getUser(), new EmoticonUploadReq(name, description, thumbnail, emoticonList));
    }

}
