package bssm.bsm.domain.board.anonymous.domain;

import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class AnonymousKey {

    private AnonymousKeyType type;
    private Long id;
    private Long userCode;

    public AnonymousKey(AnonymousKeyType type, Long id) {
        this.type = type;
        this.id = id;
        this.userCode = -1L;
    }

    @Override
    public boolean equals(Object o) {
        return type.equals(((AnonymousKey) o).type)
                && id.equals(((AnonymousKey) o).id)
                && userCode.equals(((AnonymousKey) o).userCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, userCode);
    }

}
