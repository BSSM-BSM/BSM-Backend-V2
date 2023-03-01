package bssm.bsm.domain.board.like.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Like {
    LIKE(1),
    DISLIKE(-1),
    NONE(0);

    private final int value;
}
