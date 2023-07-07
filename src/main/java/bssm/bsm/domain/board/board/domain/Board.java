package bssm.bsm.domain.board.board.domain;

import bssm.bsm.domain.board.board.exception.StudentAccessOnlyException;
import bssm.bsm.domain.board.board.exception.TeacherAccessOnlyException;
import bssm.bsm.domain.board.board.presentation.dto.res.BoardRes;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.domain.user.domain.type.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

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

    @Column(length = 12)
    @Enumerated(EnumType.STRING)
    private UserRole accessibleRole;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private final Set<PostCategory> categories = new HashSet<>();

    @Builder
    public Board(String id, String name, String subBoardId, String subBoardName, UserLevel writePostLevel, boolean publicPost, UserLevel writeCommentLevel, boolean publicComment, UserRole accessibleRole) {
        this.id = id;
        this.name = name;
        this.subBoardId = subBoardId;
        this.subBoardName = subBoardName;
        this.writePostLevel = writePostLevel;
        this.publicPost = publicPost;
        this.writeCommentLevel = writeCommentLevel;
        this.publicComment = publicComment;
        this.accessibleRole = accessibleRole;
    }

    public BoardRes toResponse(User nullableUser) {
        BoardRes.BoardResBuilder builder = BoardRes.builder()
                .boardId(id)
                .boardName(name)
                .subBoardId(subBoardId)
                .subBoardName(subBoardName)
                .categoryList(categories.stream()
                        .map(PostCategory::toResponse)
                        .toList());

        if (nullableUser == null) {
            return builder
                    .postPermission(false)
                    .commentPermission(false)
                    .build();
        }

        UserLevel level = nullableUser.getLevel();
        return builder
                .postPermission(writePostLevel.getValue() <= level.getValue())
                .commentPermission(writeCommentLevel.getValue() <= level.getValue())
                .build();
    }

    public void checkAccessibleRole(User nullableUser) {
        if (accessibleRole == null) return;
        if (nullableUser != null && accessibleRole == nullableUser.getRole()) return;

        switch (accessibleRole) {
            case STUDENT -> throw new StudentAccessOnlyException();
            case TEACHER -> throw new TeacherAccessOnlyException();
        }
    }
    
}
