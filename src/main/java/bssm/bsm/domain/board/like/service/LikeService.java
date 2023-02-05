package bssm.bsm.domain.board.like.service;

import bssm.bsm.domain.board.like.presentation.dto.req.LikeReq;
import bssm.bsm.domain.board.like.presentation.dto.res.LikeRes;
import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.like.domain.PostLikePk;
import bssm.bsm.domain.board.like.domain.repository.LikeRepository;
import bssm.bsm.domain.board.post.presentation.dto.req.PostReq;
import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.repository.PostRepository;
import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.post.service.PostProvider;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.error.exceptions.BadRequestException;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final BoardProvider boardProvider;
    private final PostProvider postProvider;

    @Transactional
    public LikeRes like(User user, PostReq postReq, @Valid LikeReq dto) {
        Board board = boardProvider.findBoard(postReq.getBoardId());
        board.checkPermissionByUserRole(user.getRole());
        Post post = postProvider.findPost(board, postReq.getPostId());

        int like = dto.getLike();
        Optional<PostLike> postLikeCheck = likeRepository.findByPkPostAndUserCode(post, user.getCode());

        // 좋아요 또는 싫어요를 누른 적이 없으면
        if (postLikeCheck.isEmpty()) {
            if (like == 0) {
                throw new BadRequestException(ImmutableMap.<String, String>builder().
                        put("like", "정상적인 요청이 아닙니다").
                        build()
                );
            }
            PostLike newLike = PostLike.builder()
                    .pk(
                            PostLikePk.builder()
                                    .id(likeRepository.countByPost(post) + 1)
                                    .post(post)
                                    .build()
                    )
                    .userCode(user.getCode())
                    .like(like)
                    .build();
            likeRepository.save(newLike);
            post.setTotalLikes(post.getTotalLikes() + like);
            postRepository.save(post);
            return LikeRes.builder()
                    .like(like)
                    .totalLikes(post.getTotalLikes())
                    .build();
        }

        PostLike postLike = postLikeCheck.get();

        // 취소한 좋아요 또는 싫어요를 다시 누름
        if (postLike.getLike() == 0) {
            post.setTotalLikes(post.getTotalLikes() + like);
            postRepository.save(post);
            postLike.setLike(like);
            likeRepository.save(postLike);
            return LikeRes.builder()
                    .like(like)
                    .totalLikes(post.getTotalLikes())
                    .build();
        }

        // 좋아요 또는 싫어요를 한번 더
        if (postLike.getLike() == like) {
            post.setTotalLikes(post.getTotalLikes() - like);
            postRepository.save(post);
            postLike.setLike(0);
            likeRepository.save(postLike);
            return LikeRes.builder()
                    .like(0)
                    .totalLikes(post.getTotalLikes())
                    .build();
        }

        // 좋아요 또는 싫어요에서 취소
        if (like == 0 && postLike.getLike() != like) {
            post.setTotalLikes(post.getTotalLikes() - postLike.getLike());
            postRepository.save(post);
            postLike.setLike(0);
            likeRepository.save(postLike);
            return LikeRes.builder()
                    .like(0)
                    .totalLikes(post.getTotalLikes())
                    .build();
        }

        // 좋아요에서 싫어요 또는 싫어요에서 좋아요
        post.setTotalLikes(post.getTotalLikes() + (like * 2));
        postRepository.save(post);
        postLike.setLike(like);
        likeRepository.save(postLike);
        return LikeRes.builder()
                .like(like)
                .totalLikes(post.getTotalLikes())
                .build();
    }
}
