package bssm.bsm.domain.board.like.repository;

import bssm.bsm.domain.board.like.entity.PostLike;
import bssm.bsm.domain.board.like.entity.PostLikePk;
import bssm.bsm.domain.board.post.entities.Post;
import bssm.bsm.domain.board.post.entities.PostPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, PostLikePk> {

    Optional<PostLike> findByPkPost(Post post);

    @Query(value = "SELECT COUNT(l) FROM PostLike l WHERE l.pk.post.pk.board.id = :boardId AND l.pk.post.pk.id = :postId")
    long countByPostPk(@Param("boardId") String boardId, @Param("postId") long postId);

    Optional<PostLike> findByPkPostPkAndUserCode(PostPk postPk, long userCode);
}
