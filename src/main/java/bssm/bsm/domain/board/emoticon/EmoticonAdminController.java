package bssm.bsm.domain.board.emoticon;

import bssm.bsm.domain.board.emoticon.dto.request.EmoticonDeleteRequestDto;
import bssm.bsm.domain.board.emoticon.dto.response.EmoticonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("admin/emoticon")
@RequiredArgsConstructor
public class EmoticonAdminController {

    private final EmoticonService emoticonService;

    @GetMapping("inactive")
    public List<EmoticonResponseDto> getInactiveEmoticonList() {
        return emoticonService.getInactiveEmoticonList();
    }

    @PutMapping("{id}/delete")
    public void deleteEmoticon(@PathVariable long id, @RequestBody EmoticonDeleteRequestDto dto) {
        emoticonService.deleteEmoticon(id, dto);
    }

    @PutMapping("{id}")
    public void activeEmoticon(@PathVariable long id) {
        emoticonService.activeEmoticon(id);
    }

}
