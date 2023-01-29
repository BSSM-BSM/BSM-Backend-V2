package bssm.bsm.domain.board.board.domain;

import bssm.bsm.domain.board.board.presentation.dto.res.BoardRes;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.type.UserLevel;
import bssm.bsm.domain.user.domain.type.UserRole;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
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

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
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

    public BoardRes toResponse(Optional<User> user) {
        BoardRes.BoardResBuilder builder = BoardRes.builder()
                .boardId(id)
                .boardName(name)
                .subBoardId(subBoardId)
                .subBoardName(subBoardName)
                .categoryList(categories.stream()
                        .map(PostCategory::toResponse)
                        .toList());

        if (user.isEmpty()) {
            return builder
                    .postPermission(false)
                    .commentPermission(false)
                    .build();
        }

        UserLevel level = user.get().getLevel();
        return builder
                .postPermission(writePostLevel.getValue() <= level.getValue())
                .commentPermission(writeCommentLevel.getValue() <= level.getValue())
                .build();
    }

    public void checkRole(UserRole role) {
        if (accessibleRole == null || accessibleRole == role) return;

        switch (accessibleRole) {
            case STUDENT -> throw new ForbiddenException("학생만 접근할 수 있는 게시판입니다");
            case TEACHER -> throw new ForbiddenException("선생님만 접근할 수 있는 게시판입니다");
        }
    }
    
}
