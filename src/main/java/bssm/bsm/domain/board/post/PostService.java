package bssm.bsm.domain.board.post;

import bssm.bsm.domain.board.like.entity.PostLike;
import bssm.bsm.domain.board.like.entity.PostLikePk;
import bssm.bsm.domain.board.like.repository.LikeRepository;
import bssm.bsm.domain.board.post.dto.PostDto;
import bssm.bsm.domain.board.post.dto.request.WritePostDto;
import bssm.bsm.domain.board.post.dto.response.PostListResponseDto;
import bssm.bsm.domain.board.post.dto.response.UploadFileResponseDto;
import bssm.bsm.domain.board.post.dto.response.ViewPostResponseDto;
import bssm.bsm.domain.board.post.entities.*;
import bssm.bsm.domain.board.post.repositories.PostRepository;
import bssm.bsm.domain.board.utils.BoardUtil;
import bssm.bsm.domain.board.utils.PostCategoryUtil;
import bssm.bsm.domain.board.post.dto.request.GetPostListDto;
import bssm.bsm.domain.board.post.dto.request.ModifyPostDto;
import bssm.bsm.domain.board.post.dto.request.PostIdDto;
import bssm.bsm.domain.user.dto.response.UserResponseDto;
import bssm.bsm.domain.user.entities.User;
import bssm.bsm.domain.user.type.UserLevel;
import bssm.bsm.global.exceptions.BadRequestException;
import bssm.bsm.global.exceptions.ForbiddenException;
import bssm.bsm.global.exceptions.InternalServerException;
import bssm.bsm.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final BoardUtil boardUtil;
    private final PostCategoryUtil categoryUtil;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    @Value("${env.file.path.base}")
    private String PUBLIC_RESOURCE_PATH;
    @Value("${env.file.path.upload.board}")
    private String BOARD_UPLOAD_RESOURCE_PATH;

    public PostListResponseDto postList(String boardId, GetPostListDto dto) {
        Board board = boardUtil.getBoard(boardId);
        PostCategoryPk postCategoryId = new PostCategoryPk(dto.getCategoryId(), board);
        final boolean pageMode = dto.getStartPostId() < 0;

        List<Post> posts;
        Page<Post> pages = null;
        if (pageMode) {
             pages = getPostListByOffset(postCategoryId, dto);
             posts = pages.getContent();
        } else {
            posts = getPostListByCursor(postCategoryId, dto);
        }

        List<PostDto> postDtoList = new ArrayList<>();
        posts.forEach(post ->
                postDtoList.add(PostDto.builder()
                            .id(post.getPk().getId())
                            .user(getUserData(post.getUser(), post.isAnonymous()))
                            .category(post.getCategoryId())
                            .title(post.getTitle())
                            .createdAt(post.getCreatedAt())
                            .hit(post.getHit())
                            .totalComments(post.getTotalComments())
                            .totalLikes(post.getTotalLikes())
                            .build()
                )
        );

        if (pageMode) {
            return PostListResponseDto.builder()
                    .posts(postDtoList)
                    .totalPages(pages.getTotalPages())
                    .page(dto.getPage())
                    .limit(dto.getLimit())
                    .build();
        } else {
            return PostListResponseDto.builder()
                    .posts(postDtoList)
                    .limit(dto.getLimit())
                    .build();
        }
    }

    public ViewPostResponseDto viewPost(User user, PostIdDto postIdDto) {
        Post post = getPost(postIdDto);
        PostLike postLike = likeRepository.findByPkPostPkAndUserCode(post.getPk(), user.getCode()).orElseGet(
                () -> PostLike.builder()
                        .like(0)
                        .build()
        );
        post.setHit(post.getHit() + 1);
        postRepository.save(post);

        return ViewPostResponseDto.builder()
                .id(postIdDto.getPostId())
                .user(getUserData(post.getUser(), post.isAnonymous()))
                .category(post.getCategoryId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .hit(post.getHit())
                .totalComments(post.getTotalComments())
                .totalLikes(post.getTotalLikes())
                .like(postLike.getLike())
                .permission(checkPermission(user, post))
                .build();
    }

    @Transactional
    public long writePost(User user, String boardId, WritePostDto dto) {
        Board board = boardUtil.getBoard(boardId);
        PostCategory postCategory = categoryUtil.getCategory(new PostCategoryPk(dto.getCategory(), board));

        Post newPost = Post.builder()
                .pk(
                        PostPk.builder()
                                .id(postRepository.countByBoardId(boardId) + 1)
                                .board(board)
                                .build()
                )
                .userCode(user.getCode())
                .categoryId(postCategory == null? null: postCategory.getPostCategoryPk().getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdAt(new Date())
                .anonymous(dto.isAnonymous())
                .build();
        postRepository.save(newPost);
        return newPost.getPk().getId();
    }

    public void modifyPost(User user, PostIdDto postIdDto, ModifyPostDto dto) {
        Board board = boardUtil.getBoard(postIdDto.getBoard());
        Post post = getPost(postIdDto);
        if (!checkPermission(user, post)) throw new ForbiddenException("권한이 없습니다");

        PostCategory postCategory = categoryUtil.getCategory(new PostCategoryPk(dto.getCategory(), board));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(postCategory);
        post.setAnonymous(dto.isAnonymous());

        postRepository.save(post);
    }

    public void deletePost(User user, PostIdDto postIdDto) {
        Post post = getPost(postIdDto);
        if (!checkPermission(user, post)) throw new ForbiddenException("권한이 없습니다");
        post.setDelete(true);

        postRepository.save(post);
    }

    public UploadFileResponseDto uploadFile(MultipartFile file) {
        if (file.getOriginalFilename() == null) throw new BadRequestException();
        String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String fileId = String.valueOf(new Date().getTime());

        File dir = new File(PUBLIC_RESOURCE_PATH + BOARD_UPLOAD_RESOURCE_PATH);
        File newFile = new File(dir.getPath() + "/" + fileId + "." + fileExt);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                throw new InternalServerException("파일 업로드에 실패하였습니다");
            }
        }

        try {
            file.transferTo(newFile);
            return UploadFileResponseDto.builder()
                    .id(fileId)
                    .fileExt(fileExt)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("파일 업로드에 실패하였습니다");
        }
    }

    private Post getPost(PostIdDto dto) {
        Board board = boardUtil.getBoard(dto.getBoard());
        return postRepository.findByPkAndDelete(new PostPk(dto.getPostId(), board), false).orElseThrow(
                () -> {throw new NotFoundException("게시글을 찾을 수 없습니다");}
        );
    }

    private boolean checkPermission(User user, Post post) {
        return Objects.equals(post.getUserCode(), user.getCode()) || user.getLevel() == UserLevel.ADMIN;
    }

    private Page<Post> getPostListByOffset(PostCategoryPk postCategoryId, GetPostListDto dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getLimit());
        // 전체 게시글
        if (postCategoryId.getId().equals("all")) {
            return postRepository.findByPkBoardAndDeleteOrderByPkIdDesc(postCategoryId.getBoard(), false, pageable);
        }
        PostCategory postCategory = categoryUtil.getCategory(postCategoryId);
        // 카테고리 없는 게시글
        if (postCategory == null) {
            return postRepository.findByPkBoardAndCategoryIdAndDeleteOrderByPkIdDesc(postCategoryId.getBoard(), null, false, pageable);
        }
        // 카테고리 있는 게시글
        return postRepository.findByCategoryAndDeleteOrderByPkIdDesc(postCategory, false, pageable);
    }

    private List<Post> getPostListByCursor(PostCategoryPk postCategoryId, GetPostListDto dto) {
        PostPk postId = new PostPk(dto.getStartPostId(), postCategoryId.getBoard());
        Pageable pageable = Pageable.ofSize(dto.getLimit());
        // 전체 게시글
        if (postCategoryId.getId().equals("all")) {
            return postRepository.findByPkLessThanAndDeleteOrderByPkIdDesc(postId, false, pageable);
        }
        PostCategory postCategory = categoryUtil.getCategory(postCategoryId);
        // 카테고리 없는 게시글
        if (postCategory == null) {
            return postRepository.findByPkLessThanAndCategoryIdAndDeleteOrderByPkIdDesc(postId, null, false, pageable);
        }
        // 카테고리 있는 게시글
        return postRepository.findByPkLessThanAndCategoryAndDeleteOrderByPkIdDesc(postId, postCategory, false, pageable);
    }

    private UserResponseDto getUserData(User user, boolean anonymous) {
        if (anonymous) {
            return UserResponseDto.builder()
                    .code((long) -1)
                    .nickname("ㅇㅇ")
                    .build();
        }
        return UserResponseDto.builder()
                .code(user.getCode())
                .nickname(user.getNickname())
                .build();
    }
}
