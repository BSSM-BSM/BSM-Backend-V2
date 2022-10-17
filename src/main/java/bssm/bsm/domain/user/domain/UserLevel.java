package bssm.bsm.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserLevel {
    USER(0),
    ADMIN(1);

    private final int value;
}
