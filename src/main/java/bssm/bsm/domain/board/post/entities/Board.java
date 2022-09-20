package bssm.bsm.domain.board.post.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(nullable = false, columnDefinition = "tinyint")
    private int writePostLevel;

    @Column(nullable = false)
    private boolean publicPost;

    @Column(nullable = false, columnDefinition = "tinyint")
    private int writeCommentLevel;

    @Column(nullable = false)
    private boolean publicComment;

    @Builder
    public Board(String id, String name, String subBoardId, String subBoardName, int writePostLevel, boolean publicPost, int writeCommentLevel, boolean publicComment) {
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
