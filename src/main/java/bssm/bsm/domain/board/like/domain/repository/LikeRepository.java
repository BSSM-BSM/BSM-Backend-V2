package bssm.bsm.domain.board.like.domain.repository;

import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndUser(Post post, User user);
}
