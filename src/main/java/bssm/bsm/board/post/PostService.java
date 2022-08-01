package bssm.bsm.board.post;

import bssm.bsm.board.post.dto.PostDto;
import bssm.bsm.board.post.dto.request.*;
import bssm.bsm.board.post.dto.response.ViewPostResponseDto;
import bssm.bsm.board.post.entities.*;
import bssm.bsm.board.post.repositories.PostRepository;
import bssm.bsm.board.utils.BoardUtil;
import bssm.bsm.board.utils.PostCategoryUtil;
import bssm.bsm.global.exceptions.ForbiddenException;
import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final BoardUtil boardUtil;
    private final PostCategoryUtil categoryUtil;
    private final PostRepository postRepository;

    public List<PostDto> postList(String boardId, GetPostListDto dto) {
        Board board = boardUtil.getBoard(boardId);
        PostCategoryId postCategoryId = new PostCategoryId(board, dto.getCategoryId());

        long totalPosts = getTotalPosts(postCategoryId);
        System.out.println(totalPosts);
        List<Post> posts = getPostList(postCategoryId);

        List<PostDto> postDtoList = new ArrayList<>();
        posts.forEach(post -> {
            postDtoList.add(PostDto.builder()
                    .user(User.builder()
                            .usercode(post.getUsercode())
                            .nickname(post.getUser().getNickname())
                            .build())
                    .title(post.getTitle())
                    .createdAt(post.getCreatedAt())
                    .hit(post.getHit())
                    .totalComments(post.getTotalComments())
                    .totalLikes(post.getTotalLikes())
                    .build()
            );
        });

        return postDtoList;
    }

    public ViewPostResponseDto viewPost(User user, PostIdDto postIdDto) {
        Post post = getPost(postIdDto);

        return ViewPostResponseDto.builder()
                .user(User.builder()
                        .usercode(post.getUsercode())
                        .nickname(post.getUser().getNickname())
                        .build())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .hit(post.getHit())
                .totalComments(post.getTotalComments())
                .totalLikes(post.getTotalLikes())
                .like(false)
                .permission(checkPermission(user, post))
                .build();
    }

    @Transactional
    public int writePost(User user, String boardId, WritePostDto dto) {
        Board board = boardUtil.getBoard(boardId);
        PostCategory postCategory = categoryUtil.getCategory(new PostCategoryId(board, dto.getCategory()));

        Post newPost = Post.builder()
                .usercode(user.getUsercode())
                .category(postCategory)
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdAt(new Date())
                .build();

        return postRepository.insertPost(newPost, boardId);
    }

    @Transactional
    public void modifyPost(User user, PostIdDto postIdDto, ModifyPostDto dto) {
        Board board = boardUtil.getBoard(postIdDto.getBoard());
        Post post = getPost(postIdDto);
        if (!checkPermission(user, post)) throw new ForbiddenException("권한이 없습니다");

        PostCategory postCategory = categoryUtil.getCategory(new PostCategoryId(board, dto.getCategory()));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(postCategory);

        postRepository.save(post);
    }

    @Transactional
    public void deletePost(User user, PostIdDto postIdDto) {
        Post post = getPost(postIdDto);
        if (!checkPermission(user, post)) throw new ForbiddenException("권한이 없습니다");
        post.setDelete(true);

        postRepository.save(post);
    }

    private Post getPost(PostIdDto dto) {
        Board board = boardUtil.getBoard(dto.getBoard());
        return postRepository.findByPostIdAndDelete(new PostId(dto.getPost(), board), false).orElseThrow(
                () -> {throw new NotFoundException("게시글을 찾을 수 없습니다");}
        );
    }

    private boolean checkPermission(User user, Post post) {
        return post.getUsercode() == user.getUsercode() || user.getLevel() >= 3;
    }

    private long getTotalPosts(PostCategoryId postCategoryId) {
        // 전체 게시글
        if (postCategoryId.getCategoryId().equals("all")) {
            return postRepository.countByPostIdBoardAndDelete(postCategoryId.getBoard(), false);
        }
        PostCategory postCategory = categoryUtil.getCategory(postCategoryId);
        // 카테고리 없는 게시글
        if (postCategory == null) {
            return postRepository.countByPostIdBoardAndCategoryIdAndDelete(postCategoryId.getBoard(), null, false);
        }
        // 카테고리 있는 게시글
        return postRepository.countByCategoryAndDelete(postCategory, false);
    }

    private List<Post> getPostList(PostCategoryId postCategoryId) {
        // 전체 게시글
        if (postCategoryId.getCategoryId().equals("all")) {
            return postRepository.findByPostIdBoardAndDelete(postCategoryId.getBoard(), false);
        }
        PostCategory postCategory = categoryUtil.getCategory(postCategoryId);
        // 카테고리 없는 게시글
        if (postCategory == null) {
            return postRepository.findByPostIdBoardAndCategoryIdAndDelete(postCategoryId.getBoard(), null, false);
        }
        // 카테고리 있는 게시글
        return postRepository.findByCategoryAndDelete(postCategory, false);
    }
}
