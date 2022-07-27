package bssm.bsm.board.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@AllArgsConstructor
@Entity
@Table
public class Board {

    @Id
    @Column(length = 10)
    private String id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 10)
    private String subBoardId;

    @Column(nullable = false, length = 10)
    private String subBoardName;

    @Column(nullable = false, columnDefinition = "tinyint")
    private int writePostLevel;

    @Column(nullable = false)
    private boolean publicPost;

    @Column(nullable = false, columnDefinition = "tinyint")
    private int writeCommentLevel;

    @Column(nullable = false)
    private boolean publicComment;
}
