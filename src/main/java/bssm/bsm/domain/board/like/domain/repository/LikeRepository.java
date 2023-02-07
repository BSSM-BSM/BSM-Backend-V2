package bssm.bsm.domain.board.like.domain.repository;

import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.like.domain.PostLikePk;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, PostLikePk> {

    @Query(value = "SELECT COUNT(l) FROM PostLike l WHERE l.post = post")
    long countByPost(@Param("post")Post post);

    Optional<PostLike> findByPostAndUser(Post post, User user);
}
