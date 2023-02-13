package bssm.bsm.domain.board.like.service;

import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.like.domain.repository.LikeRepository;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeProvider {

    private final LikeRepository likeRepository;

    public PostLike findMyPostLike(Optional<User> user, Post post) {
        if (user.isEmpty()) {
            return null;
        }
        return likeRepository.findByPostAndUser(post, user.get())
                .orElse(null);
    }

    public PostLike findMyPostLike(User user, Post post) {
        return likeRepository.findByPostAndUser(post, user)
                .orElse(null);
    }

    public long getNewLikeId(Post post) {
        return likeRepository.countByPost(post) + 1;
    }

}
