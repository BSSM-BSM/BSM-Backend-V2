package bssm.bsm.domain.board.like.service;

import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.like.domain.repository.LikeRepository;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeProvider {

    private final LikeRepository likeRepository;

    public PostLike findMyPostLike(User user, Post post) {
        return likeRepository.findByPostAndUser(post, user)
                .orElse(null);
    }

}
