package bssm.bsm.domain.board.emoticon.service;

import bssm.bsm.domain.board.emoticon.domain.Emoticon;
import bssm.bsm.domain.board.emoticon.domain.repository.EmoticonRepository;
import bssm.bsm.domain.board.emoticon.exception.AlreadyExistsEmoticonException;
import bssm.bsm.domain.board.emoticon.exception.NoSuchEmoticonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmoticonProvider {

    private final EmoticonRepository emoticonRepository;

    public Emoticon findEmoticon(long id) {
        return emoticonRepository.findById(id)
                .orElseThrow(NoSuchEmoticonException::new);
    }

    public List<Emoticon> findAllEmoticon() {
        return emoticonRepository.findAllByActiveAndDeletedOrderByTotalViewDesc(false, false);
    }

    public List<Emoticon> findAllActiveEmoticon() {
        return emoticonRepository.findAllByActiveAndDeletedOrderByTotalViewDesc(true, false);
    }

    public void duplicateEmoticonNameCheck(String name) {
        if (emoticonRepository.existsByName(name)) throw new AlreadyExistsEmoticonException();
    }
}
