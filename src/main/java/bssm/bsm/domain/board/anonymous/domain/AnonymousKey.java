package bssm.bsm.domain.board.anonymous.domain;

import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class AnonymousKey {

    private AnonymousKeyType type;
    private String sessionId;
    private Long userId;

    public AnonymousKey(AnonymousKeyType type, String sessionId) {
        this.type = type;
        this.sessionId = sessionId;
        this.userId = -1L;
    }

    @Override
    public boolean equals(Object o) {
        return type.equals(((AnonymousKey) o).type)
                && sessionId.equals(((AnonymousKey) o).sessionId)
                && userId.equals(((AnonymousKey) o).userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, sessionId, userId);
    }

}
