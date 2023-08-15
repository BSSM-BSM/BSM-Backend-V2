package bssm.bsm.domain.board.post.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostAnonymousType {
    VISIBLE(0), // 일반 게시글
    INVISIBLE(1), // 익명 게시글
    NO_RECORD(2); // 유저 데이터가 기록되지 않는 익명 게시글
    private final int value;
}
