package bssm.bsm.domain.board.like.service;

import bssm.bsm.domain.board.like.domain.enums.Like;
import bssm.bsm.domain.board.like.presentation.dto.req.LikeReq;
import bssm.bsm.domain.board.like.presentation.dto.res.LikeRes;
import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.like.domain.repository.LikeRepository;
import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.post.service.PostProvider;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeProvider likeProvider;
    private final BoardProvider boardProvider;
    private final PostProvider postProvider;

    private final LikeRepository likeRepository;

    public LikeRes like(User user, LikeReq req) {
        Board board = boardProvider.findBoard(req.getBoardId());
        board.checkAccessibleRole(user);
        Post post = postProvider.findPost(board, req.getPostId());
        Like like = req.getLike();
        PostLike prevLike = likeProvider.findMyPostLike(user, post);

        // 좋아요 또는 싫어요를 누른 적이 없으면
        if (prevLike == null) {
            saveNewLike(like, post, user);
        } else {
            updatePrevLike(prevLike, post, like);
        }
        return LikeRes.create(like, post);
    }

    private void saveNewLike(Like like, Post post, User user) {
        PostLike newLike = PostLike.create(likeProvider.getNewLikeId(post), post, user, like);
        likeRepository.save(newLike);
        if (like == Like.LIKE) post.applyPostLike();
        if (like == Like.DISLIKE) post.applyPostDislike();
    }

    private void updatePrevLike(PostLike prevLike, Post post, Like like) {
        // 좋아요 또는 싫어요를 한번 더 눌렀으면
        if (prevLike.getLike() == like) {
            return;
        }
        // 취소한 좋아요 또는 싫어요를 다시 누름
        if (prevLike.getLike() == Like.NONE && like != Like.NONE) {
            if (like == Like.LIKE) post.applyPostLike();
            if (like == Like.DISLIKE) post.applyPostDislike();
        }
        // 좋아요 또는 싫어요 취소
        if (like == Like.NONE && prevLike.getLike() != like) {
            post.cancelPostLike(prevLike.getLike());
        }
        // 좋아요에서 싫어요 또는 싫어요에서 좋아요
        if (prevLike.getLike() != like && like != Like.NONE) {
            post.reservePostLike(prevLike.getLike());
        }

        prevLike.updateLike(like);
    }
}
