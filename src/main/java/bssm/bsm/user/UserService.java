package bssm.bsm.user;

import bssm.bsm.user.dto.request.UserSignUpDto;
import bssm.bsm.user.entities.Student;
import bssm.bsm.user.entities.User;
import bssm.bsm.user.repositories.StudentRepository;
import bssm.bsm.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HexFormat;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public User signUp(UserSignUpDto dto) throws Exception {
        User user = dto.toEntity();

        if (!dto.getPw().equals(dto.getCheckPw())) {
            throw new Exception("비밀번호 재입력이 맞지 않습니다");
        }

        validateDuplicateUser(user);
        Student studentInfo = studentRepository.findByAuthCode(dto.getAuthCode())
                .orElseThrow(() -> {
                    throw new IllegalStateException("인증코드를 찾을 수 없습니다");
                });
        if (!studentInfo.isCodeAvailable()) {
            throw new IllegalStateException("이미 사용된 인증코드입니다");
        }

        studentInfo.setCodeAvailable(false);
        user.setUniqNo(studentInfo.getUniqNo());
        user.setLevel(studentInfo.getLevel());
        user.setCreatedAt(new Date());

        // 비밀번호 솔트 값 생성
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String salt = HexFormat.of().formatHex(randomBytes);

        // 비밀번호 암호화
        MessageDigest messageDigest = MessageDigest.getInstance("SHA3-256");
        messageDigest.update((salt + dto.getPw()).getBytes(StandardCharsets.UTF_8));

        user.setPwSalt(salt);
        user.setPw(HexFormat.of().formatHex(messageDigest.digest()));
        userRepository.save(user);
        return user;
    }

    private void validateDuplicateUser(User user) {
        userRepository.findById(user.getId())
                .ifPresent(u -> {
                    throw new IllegalStateException("이미 존재하는 ID 입니다");
                });
        userRepository.findByNickname(user.getNickname())
                .ifPresent(u -> {
                    throw new IllegalStateException("이미 존재하는 닉네임 입니다");
                });
    }
}
