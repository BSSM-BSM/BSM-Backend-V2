package bssm.bsm.domain.board.like;

import bssm.bsm.domain.board.like.dto.request.LikeRequestDto;
import bssm.bsm.domain.board.like.dto.response.LikeResponseDto;
import bssm.bsm.domain.board.like.entity.PostLike;
import bssm.bsm.domain.board.like.entity.PostLikePk;
import bssm.bsm.domain.board.like.repository.LikeRepository;
import bssm.bsm.domain.board.post.dto.request.PostIdDto;
import bssm.bsm.domain.board.post.entities.Board;
import bssm.bsm.domain.board.post.entities.Post;
import bssm.bsm.domain.board.post.entities.PostPk;
import bssm.bsm.domain.board.post.repositories.PostRepository;
import bssm.bsm.domain.board.utils.BoardUtil;
import bssm.bsm.domain.user.entities.User;
import bssm.bsm.global.exceptions.BadRequestException;
import bssm.bsm.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final BoardUtil boardUtil;

    @Transactional
    public LikeResponseDto like(User user, PostIdDto postIdDto, @Valid LikeRequestDto dto) {
        Board board = boardUtil.getBoard(postIdDto.getBoard());
        PostPk postId = new PostPk(postIdDto.getPostId(), board);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> {throw new NotFoundException("게시글을 찾을 수 없습니다");}
        );
        int like = dto.getLike();

        Optional<PostLike> postLikeCheck = likeRepository.findByPkPost(post);

        // 좋아요 또는 싫어요를 누른 적이 없으면
        if (postLikeCheck.isEmpty()) {
            if (like == 0) throw new BadRequestException();
            PostLike newLike = PostLike.builder()
                    .pk(
                            PostLikePk.builder()
                                    .id(likeRepository.countByPostPk(board.getId(), postId.getId()))
                                    .post(post)
                                    .build()
                    )
                    .userCode(user.getCode())
                    .like(like)
                    .build();
            likeRepository.save(newLike);
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
