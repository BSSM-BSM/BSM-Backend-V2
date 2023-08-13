package bssm.bsm.domain.board.anonymous.service;

import bssm.bsm.domain.board.anonymous.domain.AnonymousKey;
import bssm.bsm.domain.board.anonymous.domain.AnonymousKeyType;
import bssm.bsm.domain.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AnonymousUserIdProvider {

    // key: user id -> value: anonymous user id
    private final Map<AnonymousKey, Long> anonymousMap = new HashMap<>();
    // key: key -> value: new anonymous user id (새로운 익명 id 발급 시 1씩 자동 증가)
    private final Map<AnonymousKey, Long> anonymousIdMap = new HashMap<>();

    public long getAnonymousId(AnonymousKeyType type, String sessionId, User user) {
        AnonymousKey key = new AnonymousKey(type, sessionId, user.getCode());
        Long id = Optional.ofNullable(anonymousMap.get(key))
                .orElseGet(() -> getNewId(type, sessionId));
        anonymousMap.put(key, id);
        return id;
    }

    private long getNewId(AnonymousKeyType type, String sessionId) {
        AnonymousKey idKey = new AnonymousKey(type, sessionId);
        Long newId = Optional.ofNullable(anonymousIdMap.get(idKey))
                .orElseGet(() -> 0L);
        anonymousIdMap.put(idKey, ++newId);
        return newId;
    }

}
