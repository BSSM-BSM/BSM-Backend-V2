package bssm.bsm.board.like;

import bssm.bsm.board.like.dto.request.LikeRequestDto;
import bssm.bsm.board.like.dto.response.LikeResponseDto;
import bssm.bsm.board.like.entity.PostLike;
import bssm.bsm.board.like.repository.LikeRepository;
import bssm.bsm.board.post.dto.request.PostIdDto;
import bssm.bsm.board.post.entities.Board;
import bssm.bsm.board.post.entities.Post;
import bssm.bsm.board.post.entities.PostPk;
import bssm.bsm.board.post.repositories.PostRepository;
import bssm.bsm.board.utils.BoardUtil;
import bssm.bsm.global.exceptions.BadRequestException;
import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final BoardUtil boardUtil;

    public LikeResponseDto like(User user, PostIdDto postIdDto, LikeRequestDto dto) {
        Board board = boardUtil.getBoard(postIdDto.getBoard());
        PostPk postId = new PostPk(postIdDto.getPostId(), board);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> {throw new NotFoundException("게시글을 찾을 수 없습니다");}
        );
        int like = dto.getLike();
        if (like > 0) {
            like = 1;
        } else if (like < 0) {
            like = -1;
        } else {
            like = 0;
        }

        Optional<PostLike> postLikeCheck = likeRepository.findById(postId);

        // 좋아요 또는 싫어요를 누른 적이 없으면
        if (postLikeCheck.isEmpty()) {
            if (like == 0) throw new BadRequestException();
            likeRepository.save(
                    PostLike.builder()
                            .postPk(postId)
                            .usercode(user.getUsercode())
                            .like(like)
                            .build()
            );
            post.setTotalLikes(post.getTotalLikes() + like);
            postRepository.save(post);
            return LikeResponseDto.builder()
                    .like(like)
                    .totalLikes(post.getTotalLikes())
                    .build();
        }

        PostLike postLike = postLikeCheck.get();

        // 취소한 좋아요 또는 싫어요를 다시 누름
        if (postLike.getLike() == 0) {
            post.setTotalLikes(post.getTotalLikes() + like);
            postRepository.save(post);
            postLike.setLike(like);
            likeRepository.save(postLike);
            return LikeResponseDto.builder()
                    .like(like)
                    .totalLikes(post.getTotalLikes())
                    .build();
        }

        // 좋아요 또는 싫어요를 한번 더
        if (postLike.getLike() == like) {
            post.setTotalLikes(post.getTotalLikes() - like);
            postRepository.save(post);
            postLike.setLike(0);
            likeRepository.save(postLike);
            return LikeResponseDto.builder()
                    .like(0)
                    .totalLikes(post.getTotalLikes())
                    .build();
        }

        // 좋아요 또는 싫어요에서 취소
        if (like == 0 && postLike.getLike() != like) {
            post.setTotalLikes(post.getTotalLikes() - postLike.getLike());
            postRepository.save(post);
            postLike.setLike(0);
            likeRepository.save(postLike);
            return LikeResponseDto.builder()
                    .like(0)
                    .totalLikes(post.getTotalLikes())
                    .build();
        }

        // 좋아요에서 싫어요 또는 싫어요에서 좋아요
        post.setTotalLikes(post.getTotalLikes() + (like * 2));
        postRepository.save(post);
        postLike.setLike(like);
        likeRepository.save(postLike);
        return LikeResponseDto.builder()
                .like(like)
                .totalLikes(post.getTotalLikes())
                .build();
    }
}
