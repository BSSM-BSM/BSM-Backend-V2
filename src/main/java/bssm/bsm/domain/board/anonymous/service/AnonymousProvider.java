package bssm.bsm.domain.board.anonymous.service;

import bssm.bsm.domain.board.anonymous.domain.AnonymousKey;
import bssm.bsm.domain.board.anonymous.domain.AnonymousKeyType;
import bssm.bsm.domain.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AnonymousProvider {

    private final Map<AnonymousKey, Long> anonymousMap = new HashMap<>();
    private final Map<AnonymousKey, Long> anonymousIdxMap = new HashMap<>();

    public long getAnonymousIdx(AnonymousKeyType type, Long id, User user) {
        AnonymousKey key = new AnonymousKey(type, id, user.getCode());
        Long idx = Optional.ofNullable(anonymousMap.get(key))
                        .orElseGet(() -> getNewIdx(type, id));
        anonymousMap.put(key, idx);
        return idx;
    }

    private long getNewIdx(AnonymousKeyType type, Long id) {
        AnonymousKey idxKey = new AnonymousKey(type, id);
        Long newIdx = Optional.ofNullable(anonymousIdxMap.get(idxKey))
                        .orElseGet(() -> 0L);
        anonymousIdxMap.put(idxKey, ++newIdx);
        return newIdx;
    }

}
