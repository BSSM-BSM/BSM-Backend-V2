package bssm.bsm.domain.board.anonymous.domain;

import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class AnonymousKey {

    private AnonymousKeyType type;
    private String sessionId;
    private Long userCode;

    public AnonymousKey(AnonymousKeyType type, String sessionId) {
        this.type = type;
        this.sessionId = sessionId;
        this.userCode = -1L;
    }

    @Override
    public boolean equals(Object o) {
        return type.equals(((AnonymousKey) o).type)
                && sessionId.equals(((AnonymousKey) o).sessionId)
                && userCode.equals(((AnonymousKey) o).userCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, sessionId, userCode);
    }

}
