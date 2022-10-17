package bssm.bsm.domain.board.comment.facade;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import bssm.bsm.global.error.exceptions.UnAuthorizedException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommentFacade {

    public void checkViewPermission(Board board, Optional<User> user) {
        if (!board.isPublicComment() && user.isEmpty()) throw new UnAuthorizedException();
    }

    public void checkWritePermission(Board board, User user) {
        if (board.getWriteCommentLevel().getValue() > user.getLevel().getValue()) throw new ForbiddenException("권한이 없습니다");
    }

}
