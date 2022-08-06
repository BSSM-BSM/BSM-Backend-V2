package bssm.bsm.board.service;

import bssm.bsm.board.post.PostService;
import bssm.bsm.board.post.dto.request.WritePostDto;
import bssm.bsm.board.post.entities.Board;
import bssm.bsm.board.post.entities.Post;
import bssm.bsm.board.post.entities.PostPk;
import bssm.bsm.board.post.repositories.PostRepository;
import bssm.bsm.user.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("게시글 작성 테스트")
    void writePost() {
        WritePostDto dto = new WritePostDto("test", "게시글 제목", "게시글 내용", false);
        User user = User.builder()
                .usercode(10)
                .build();

        int newPostId = postService.writePost(user, "test", dto);

        PostPk postId = PostPk.builder()
                .id(newPostId)
                .board(Board.builder().id("test").build())
                .build();
        Post newPost = postRepository.findById(postId).orElseThrow(
                () -> {throw new IllegalStateException("게시글 작성 테스트 실패");}
        );

        if (!newPost.getTitle().equals("게시글 제목")) {
            throw new IllegalStateException("게시글 작성 테스트 실패");
        }
    }
}
