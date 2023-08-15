package bssm.bsm.domain.board.like.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeType {
    LIKE(1),
    DISLIKE(-1),
    NONE(0);

    private final int value;
}
