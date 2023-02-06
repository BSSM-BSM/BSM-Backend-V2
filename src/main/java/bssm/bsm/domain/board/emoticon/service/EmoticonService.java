package bssm.bsm.domain.board.emoticon.service;

import bssm.bsm.domain.board.emoticon.presentation.dto.res.EmoticonRes;
import bssm.bsm.domain.board.emoticon.domain.Emoticon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class EmoticonService {

    private final EmoticonProvider emoticonProvider;

    @Transactional(readOnly = true)
    public EmoticonRes getEmoticon(long id) {
        return emoticonProvider.findEmoticon(id)
                .toResponse();
    }

    @Transactional(readOnly = true)
    public List<EmoticonRes> getEmoticonList() {
        List<Emoticon> emoticonList = emoticonProvider.findAllActiveEmoticon();
        return emoticonList.stream()
                .map(Emoticon::toResponse)
                .toList();
    }

}
