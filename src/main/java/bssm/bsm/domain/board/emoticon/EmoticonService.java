package bssm.bsm.domain.board.emoticon;

import bssm.bsm.domain.board.emoticon.dto.response.EmoticonItemResponseDto;
import bssm.bsm.domain.board.emoticon.dto.response.EmoticonResponseDto;
import bssm.bsm.domain.board.emoticon.entities.Emoticon;
import bssm.bsm.domain.board.emoticon.repositories.EmoticonRepository;
import bssm.bsm.global.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmoticonService {

    private final EmoticonRepository emoticonRepository;

    public EmoticonResponseDto getEmoticon(long id) {
        return emoticonRepository.findById(id).orElseThrow(
                () -> {throw new NotFoundException("이모티콘을 찾을 수 없습니다");}
        ).toDto();
    }

    public List<EmoticonResponseDto> getEmoticonList() {
        List<Emoticon> emoticonList = emoticonRepository.findAll();
        return emoticonList
                .stream().map(Emoticon::toDto)
                .collect(Collectors.toList());
    }
}
