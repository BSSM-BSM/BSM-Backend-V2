package bssm.bsm.board.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
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
}
