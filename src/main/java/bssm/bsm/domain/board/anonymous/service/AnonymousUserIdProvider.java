package bssm.bsm.domain.board.anonymous.service;

import bssm.bsm.domain.board.anonymous.domain.AnonymousKey;
import bssm.bsm.domain.board.anonymous.domain.AnonymousKeyType;
import bssm.bsm.domain.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AnonymousUserIdProvider {

    private final Map<AnonymousKey, Long> anonymousMap = new HashMap<>();
    private final Map<AnonymousKey, Long> anonymousIdxMap = new HashMap<>();

    public long getAnonymousId(AnonymousKeyType type, String sessionId, User user) {
        AnonymousKey key = new AnonymousKey(type, sessionId, user.getCode());
        Long id = Optional.ofNullable(anonymousMap.get(key))
                .orElseGet(() -> getNewIdx(type, sessionId));
        anonymousMap.put(key, id);
        return id;
    }

    private long getNewIdx(AnonymousKeyType type, String sessionId) {
        AnonymousKey idKey = new AnonymousKey(type, sessionId);
        Long newId = Optional.ofNullable(anonymousIdxMap.get(idKey))
                .orElseGet(() -> 0L);
        anonymousIdxMap.put(idKey, ++newId);
        return newId;
    }

}
