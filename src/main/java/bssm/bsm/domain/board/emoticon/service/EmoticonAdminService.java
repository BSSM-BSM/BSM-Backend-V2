package bssm.bsm.domain.board.emoticon.service;

import bssm.bsm.domain.board.emoticon.domain.Emoticon;
import bssm.bsm.domain.board.emoticon.domain.repository.EmoticonRepository;
import bssm.bsm.domain.board.emoticon.exception.NoSuchEmoticonException;
import bssm.bsm.domain.board.emoticon.presentation.dto.req.EmoticonDeleteReq;
import bssm.bsm.domain.board.emoticon.presentation.dto.res.EmoticonRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class EmoticonAdminService {

    private final EmoticonRepository emoticonRepository;

    @Transactional(readOnly = true)
    public List<EmoticonRes> getInactiveEmoticonList() {
        List<Emoticon> emoticonList = emoticonRepository.findAllByActiveAndDeleted(false, false);
        return emoticonList.stream()
                .map(Emoticon::toResponse)
                .toList();
    }

    public void activateEmoticon(long id) {
        Emoticon emoticon = emoticonRepository.findById(id)
                .orElseThrow(NoSuchEmoticonException::new);
        emoticon.activate();
    }

    public void deleteEmoticon(EmoticonDeleteReq dto) {
        Emoticon emoticon = emoticonRepository.findById(dto.getId())
                .orElseThrow(NoSuchEmoticonException::new);
        emoticon.delete(dto.getMsg());
    }
}
