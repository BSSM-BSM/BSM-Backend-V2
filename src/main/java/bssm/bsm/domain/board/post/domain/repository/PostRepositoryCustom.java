package bssm.bsm.domain.board.post.domain.repository;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findPostList(Board board, Long startPostId, int limit, String category);
}