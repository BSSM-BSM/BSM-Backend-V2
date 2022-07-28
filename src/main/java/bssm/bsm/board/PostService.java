package bssm.bsm.board;

import bssm.bsm.board.dto.request.WritePostDto;
import bssm.bsm.board.entities.Post;
import bssm.bsm.board.repositories.PostRepository;
import bssm.bsm.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public int writePost(User user, WritePostDto dto) {
        Post newPost = Post.builder()
                .usercode(user.getUsercode())
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdAt(new Date())
                .build();

        return postRepository.insertPost(newPost, dto.getBoardId(), dto.getCategoryId());
    }
}
