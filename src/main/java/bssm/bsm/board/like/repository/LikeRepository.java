package bssm.bsm.board.like.repository;

import bssm.bsm.board.like.entity.PostLike;
import bssm.bsm.board.post.entities.PostId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<PostLike, PostId> {}
