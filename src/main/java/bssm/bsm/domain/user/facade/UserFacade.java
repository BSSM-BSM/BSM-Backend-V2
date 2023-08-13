package bssm.bsm.domain.user.facade;

import bssm.bsm.domain.auth.exception.NoSuchRefreshTokenException;
import bssm.bsm.domain.user.domain.Student;
import bssm.bsm.domain.auth.domain.repository.RefreshTokenRepository;
import bssm.bsm.domain.user.domain.UserCache;
import bssm.bsm.domain.user.domain.repository.StudentRepository;
import bssm.bsm.domain.user.domain.repository.RedisUserRepository;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.repository.UserRepository;
import bssm.bsm.domain.user.exception.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;
    private final RedisUserRepository userRedisRepository;
    private final StudentRepository studentRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public List<User> findAllByGradeAndClassNo(int grade, int classNo) {
        return studentRepository.findAllByGradeAndClassNo(grade, classNo).stream()
                .map(Student::getUser)
                .toList();
    }

    public User findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByTokenAndIsAvailable(refreshToken, true)
                .orElseThrow(NoSuchRefreshTokenException::new)
                .getUser();
    }

    public User findByCode(long userCode) {
        return userRepository.findById(userCode)
                .orElseThrow(NoSuchUserException::new);
    }

    public User findByCodeOrNull(long userCode) {
        return userRepository.findById(userCode)
                .orElseGet(() -> null);
    }

    public User findCachedUserByCode(long userCode) {
        UserCache userCache = userRedisRepository.findById(userCode)
                .orElseGet(() -> findAndSaveUserCache(userCode));
        return User.ofCache(userCache);
    }

    public void saveUserCache(User user) {
        userRedisRepository.save(UserCache.ofUser(user));
    }

    private UserCache findAndSaveUserCache(long userCode) {
        User user = findByCode(userCode);
        saveUserCache(user);
        return UserCache.ofUser(user);
    }

}
