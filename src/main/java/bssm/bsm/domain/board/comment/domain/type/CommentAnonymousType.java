package bssm.bsm.domain.board.comment.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentAnonymousType {
    VISIBLE(0), // 일반 댓글
    INVISIBLE(1), // 익명 댓글
    NO_RECORD(2); // 유저 데이터가 기록되지 않는 익명 댓글
    private final int value;
}
