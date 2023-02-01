package bssm.bsm.domain.board.post.service;

import bssm.bsm.domain.board.board.domain.Board;
import bssm.bsm.domain.board.category.domain.PostCategory;
import bssm.bsm.domain.board.like.domain.PostLike;
import bssm.bsm.domain.board.like.service.LikeProvider;
import bssm.bsm.domain.board.post.domain.Post;
import bssm.bsm.domain.board.post.domain.PostPk;
import bssm.bsm.domain.board.post.presentation.dto.res.PostRes;
import bssm.bsm.domain.board.post.presentation.dto.request.WritePostRequest;
import bssm.bsm.domain.board.post.presentation.dto.res.PostListRes;
import bssm.bsm.domain.board.post.presentation.dto.res.UploadFileRes;
import bssm.bsm.domain.board.post.presentation.dto.res.DetailPostRes;
import bssm.bsm.domain.board.post.facade.PostFacade;
import bssm.bsm.domain.board.post.domain.PostRepository;
import bssm.bsm.domain.board.board.service.BoardProvider;
import bssm.bsm.domain.board.category.service.CategoryProvider;
import bssm.bsm.domain.board.post.presentation.dto.request.GetPostListRequest;
import bssm.bsm.domain.board.post.presentation.dto.request.PostIdRequest;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.service.UserResProvider;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import bssm.bsm.global.error.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Validated
@RequiredArgsConstructor
public class PostService {

    private final BoardProvider boardProvider;
    private final CategoryProvider categoryUtil;
    private final PostRepository postRepository;
    private final PostProvider postProvider;
    private final LikeProvider likeProvider;
    private final PostFacade postFacade;
    private final UserResProvider userResProvider;

    @Value("${env.file.path.base}")
    private String PUBLIC_RESOURCE_PATH;
    @Value("${env.file.path.upload.board}")
    private String BOARD_UPLOAD_RESOURCE_PATH;

    public PostListRes postList(Optional<User> user, String boardId, @Valid GetPostListRequest dto) {
        Board board = boardProvider.getBoard(boardId);
        board.checkRole(user.map(User::getRole).orElse(null));
        postFacade.checkViewPermission(board, user);

        final boolean pageMode = dto.getStartPostId() < 0;

        List<Post> posts;
        Page<Post> pages = null;
        if (pageMode) {
             pages = postProvider.getPostListByOffset(board, dto);
             posts = pages.getContent();
        } else {
            posts = postProvider.getPostListByCursor(board, dto);
        }

        List<PostRes> postDtoList = new ArrayList<>();
        posts.forEach(post ->
                postDtoList.add(PostRes.builder()
                            .id(post.getPk().getId())
                            .user(userResProvider.toBoardUserRes(post.getWriter(), post.isAnonymous()))
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
            return PostListRes.builder()
                    .posts(postDtoList)
                    .totalPages(pages.getTotalPages())
                    .page(dto.getPage())
                    .limit(dto.getLimit())
                    .build();
        } else {
            return PostListRes.builder()
                    .posts(postDtoList)
                    .limit(dto.getLimit())
                    .build();
        }
    }

    public DetailPostRes viewPost(Optional<User> user, PostIdRequest postId) {
        Board board = boardProvider.getBoard(postId.getBoard());;
        postFacade.checkViewPermission(board, user);
        Post post = postProvider.getPost(postId);
        PostLike postLike = likeProvider.getMyPostLike(user, post);
        post.setHit(post.getHit() + 1);
        postRepository.save(post);

        return DetailPostRes.builder()
                .id(postId.getPostId())
                .user(userResProvider.toBoardUserRes(post.getWriter(), post.isAnonymous()))
                .category(post.getCategoryId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .hit(post.getHit())
                .totalComments(post.getTotalComments())
                .totalLikes(post.getTotalLikes())
                .like(postLike.getLike())
                .permission(user.isPresent() && post.checkPermission(user.get()))
                .anonymous(post.isAnonymous())
                .build();
    }

    @Transactional
    public long writePost(User user, String boardId, WritePostRequest dto) {
        Board board = boardProvider.getBoard(boardId);
        board.checkRole(user.getRole());
        postFacade.checkWritePermission(board, user);

        PostCategory postCategory = categoryUtil.getCategory(dto.getCategory(), board);

        Post newPost = Post.builder()
                .pk(PostPk.builder()
                        .id(postRepository.countByBoard(board) + 1)
                        .build())
                .board(board)
                .writer(user)
                .category(postCategory)
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdAt(new Date())
                .anonymous(dto.isAnonymous())
                .build();
        postRepository.save(newPost);
        return newPost.getPk().getId();
    }

    @Transactional
    public void modifyPost(User user, PostIdRequest postId, @Valid WritePostRequest dto) {
        Board board = boardProvider.getBoard(postId.getBoard());
        Post post = postProvider.getPost(postId);
        if (!post.checkPermission(user)) throw new ForbiddenException("권한이 없습니다");

        PostCategory postCategory = categoryUtil.getCategory(dto.getCategory(), board);

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(postCategory);
        post.setAnonymous(dto.isAnonymous());

        postRepository.save(post);
    }

    @Transactional
    public void deletePost(User user, PostIdRequest postId) {
        Post post = postProvider.getPost(postId);
        if (!post.checkPermission(user)) throw new ForbiddenException("권한이 없습니다");
        post.setDelete(true);
        postRepository.save(post);
    }

    public UploadFileRes uploadFile(MultipartFile file) {
        String fileExt = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
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
            return UploadFileRes.builder()
                    .id(fileId)
                    .fileExt(fileExt)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("파일 업로드에 실패하였습니다");
        }
    }

}
