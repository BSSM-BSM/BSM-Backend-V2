package bssm.bsm.domain.board.like.service;

import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.like.domain.LikeRepository;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LikeProvider {

    private final LikeRepository likeRepository;

    public PostLike getMyPostLike(Optional<User> user, Post post) {
        if (user.isEmpty()) {
            return PostLike.builder()
                    .like(0)
                    .build();
        }
        return likeRepository.findByPkPostPkAndUserCode(post.getPk(), user.get().getCode()).orElseGet(
                () -> PostLike.builder()
                        .like(0)
                        .build()
        );
    }

}
