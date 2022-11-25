package bssm.bsm.domain.user.facade;

import bssm.bsm.domain.board.anonymous.domain.AnonymousKeyType;
import bssm.bsm.domain.board.anonymous.service.AnonymousProvider;
import bssm.bsm.domain.board.comment.domain.Comment;
import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.user.domain.repository.RefreshTokenRepository;
import bssm.bsm.domain.user.domain.repository.StudentRepository;
import bssm.bsm.domain.user.domain.repository.RedisUserRepository;
import bssm.bsm.domain.user.presentation.dto.response.UserResponse;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final StudentRepository studentRepository;
    private final RedisUserRepository userRedisRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AnonymousProvider anonymousProvider;

    public UserResponse toBoardUserResponse(User user, boolean anonymous) {
        if (anonymous) {
            return UserResponse.builder()
                    .code((long) -1)
                    .nickname("ㅇㅇ")
                    .build();
        }
        return UserResponse.builder()
                .code(user.getCode())
                .nickname(user.getNickname())
                .build();
    }

    public UserResponse toCommentUserResponse(Comment comment) {
        User user = comment.getUser();
        long postId = comment.getPk().getPost().getPk().getId();
        if (comment.isAnonymous()) {
            return UserResponse.builder()
                    .code(-1L)
                    .nickname("ㅇㅇ(" + anonymousProvider.getAnonymousIdx(AnonymousKeyType.COMMENT, postId, user) + ")")
                    .build();
        }
        return UserResponse.builder()
                .code(user.getCode())
                .nickname(user.getNickname())
                .build();
    }

    public List<User> findAllByGradeAndClassNo(int grade, int classNo) {
        return studentRepository.findAllByGradeAndClassNo(grade, classNo)
                .stream().map(Student::getUser)
                .toList();
    }

    public User getByAvailableRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByTokenAndIsAvailable(refreshToken, true)
                .orElseThrow(() -> new NotFoundException("토큰을 찾을 수 없습니다"))
                .getUser();
    }

    public User getCachedUserByCode(long userCode) {
        return userRedisRepository.findById(userCode)
                .orElseThrow(NotFoundException::new)
                .toUser();
    }

    public void saveCacheUser(User user) {
        userRedisRepository.save(user.toUserRedis());
    }

}
