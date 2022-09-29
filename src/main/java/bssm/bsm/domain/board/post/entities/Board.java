package bssm.bsm.domain.board.post.entities;

import bssm.bsm.domain.user.type.UserLevel;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @Column(length = 10)
    private String id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(length = 10)
    private String subBoardId;

    @Column(length = 10)
    private String subBoardName;

    @Column(nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private UserLevel writePostLevel;

    @Column(nullable = false)
    private boolean publicPost;

    @Column(nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private UserLevel writeCommentLevel;

    @Column(nullable = false)
    private boolean publicComment;

    @Builder
    public Board(String id, String name, String subBoardId, String subBoardName, UserLevel writePostLevel, boolean publicPost, UserLevel writeCommentLevel, boolean publicComment) {
        this.id = id;
        this.name = name;
        this.subBoardId = subBoardId;
        this.subBoardName = subBoardName;
        this.writePostLevel = writePostLevel;
        this.publicPost = publicPost;
        this.writeCommentLevel = writeCommentLevel;
        this.publicComment = publicComment;
    }
}
