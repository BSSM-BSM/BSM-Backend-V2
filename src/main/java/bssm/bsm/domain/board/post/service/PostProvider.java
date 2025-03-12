package bssm.bsm.domain.board.post.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.exception.NoSuchPostException;
import bssm.bsm.domain.board.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostProvider {
    private final PostRepository postRepository;

    public Post findPost(Board board, long postId) {
        return postRepository.findByIdAndBoard(postId, board)
                .orElseThrow(NoSuchPostException::new);
    }

    public List<Post> findRecentPostList(Board board, int limit, String category) {
        return postRepository.findPostList(board, null, limit, category);
    }

    public List<Post> findPostListByCursor(Board board, long startPostId, int limit, String category) {
        return postRepository.findPostList(board, startPostId, limit, category);
    }

}
