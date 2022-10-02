package bssm.bsm.domain.board.emoticon;

import bssm.bsm.domain.board.emoticon.dto.response.EmoticonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("emoticon")
@RequiredArgsConstructor
public class EmoticonController {

    private final EmoticonService emoticonService;

    @GetMapping("{id}")
    public EmoticonResponseDto getEmoticon(@PathVariable long id) {
        return emoticonService.getEmoticon(id);
    }

    @GetMapping
    public List<EmoticonResponseDto> getEmoticonList() {
        return emoticonService.getEmoticonList();
    }
}
