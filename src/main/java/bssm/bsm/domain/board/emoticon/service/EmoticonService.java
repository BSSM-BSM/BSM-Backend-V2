package bssm.bsm.domain.board.emoticon.service;

import bssm.bsm.domain.board.emoticon.presentation.dto.res.EmoticonItemRes;
import bssm.bsm.domain.board.emoticon.presentation.dto.res.EmoticonRes;
import bssm.bsm.domain.board.emoticon.domain.Emoticon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmoticonService {

    private final EmoticonProvider emoticonProvider;

    @Transactional
    public List<EmoticonItemRes> getEmoticon(long id) {
        Emoticon emoticon = emoticonProvider.findEmoticon(id);
        emoticon.incrementTotalView();

        return emoticon.getItems().stream()
                .map(EmoticonItemRes::create)
                .toList();
    }

    public List<EmoticonRes> getEmoticonList() {
        List<Emoticon> emoticonList = emoticonProvider.findAllActiveEmoticon();
        return emoticonList.stream()
                .map(Emoticon::toResponse)
                .toList();
    }

}
