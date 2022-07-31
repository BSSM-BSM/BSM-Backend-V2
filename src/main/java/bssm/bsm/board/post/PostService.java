package bssm.bsm.board.post;

import bssm.bsm.board.post.dto.request.ViewPostDto;
import bssm.bsm.board.post.dto.request.WritePostDto;
import bssm.bsm.board.post.dto.response.ViewPostResponseDto;
import bssm.bsm.board.post.entities.Board;
import bssm.bsm.board.post.entities.Post;
import bssm.bsm.board.post.entities.PostId;
import bssm.bsm.board.post.repositories.PostRepository;
import bssm.bsm.board.utils.BoardUtil;
import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PostService {

    private final BoardUtil boardUtil;
    private final PostRepository postRepository;

    public ViewPostResponseDto viewPost(User user, ViewPostDto dto) {
        Board board = boardUtil.getBoard(dto.getBoardId());
        Post post = postRepository.findById(new PostId(dto.getPostId(), board)).orElseThrow(
                () -> {throw new NotFoundException("게시글을 찾을 수 없습니다");}
        );

        return ViewPostResponseDto.builder()
                .user(User.builder()
                        .usercode(user.getUsercode())
                        .nickname(user.getNickname())
                        .build())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .hit(post.getHit())
                .totalComments(post.getTotalComments())
                .totalLikes(post.getTotalLikes())
                .like(false)
                .permission(post.getUsercode() == user.getUsercode() || user.getLevel() >= 3)
                .build();
    }

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
