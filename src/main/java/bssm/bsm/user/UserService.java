package bssm.bsm.user;

import bssm.bsm.global.exceptions.BadRequestException;
import bssm.bsm.global.exceptions.ConflictException;
import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.user.dto.request.UserLoginDto;
import bssm.bsm.user.dto.request.UserSignUpDto;
import bssm.bsm.user.dto.request.UserUpdateNicknameDto;
import bssm.bsm.user.dto.request.UserUpdatePwDto;
import bssm.bsm.user.entities.Student;
import bssm.bsm.user.entities.User;
import bssm.bsm.user.repositories.StudentRepository;
import bssm.bsm.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public User signUp(UserSignUpDto dto) throws Exception {
        User user = dto.toEntity();

        if (!dto.getPw().equals(dto.getCheckPw())) {
            throw new BadRequestException("비밀번호 재입력이 맞지 않습니다");
        }

        userRepository.findById(user.getId())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 ID 입니다");});
        userRepository.findByNickname(user.getNickname())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 닉네임 입니다");});

        Student studentInfo = studentRepository.findByAuthCode(dto.getAuthCode())
                .orElseThrow(() -> {throw new NotFoundException("인증코드를 찾을 수 없습니다");});
        if (!studentInfo.isCodeAvailable()) {
            throw new BadRequestException("이미 사용된 인증코드입니다");
        }

        studentInfo.setCodeAvailable(false);
        user.setUniqNo(studentInfo.getUniqNo());
        user.setLevel(studentInfo.getLevel());
        user.setCreatedAt(new Date());

        // 비밀번호 솔트 값 생성
        String salt = createSalt();
        // 비밀번호 암호화
        String newPw = encryptPw(salt, dto.getPw());

        user.setPwSalt(salt);
        user.setPw(newPw);
        userRepository.save(user);

        return user;
    }

    public User login(UserLoginDto dto) throws Exception {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> {throw new BadRequestException("id 또는 password가 맞지 않습니다");});
        if (!user.getPw().equals(encryptPw(user.getPwSalt(), dto.getPw()))) {
            throw new BadRequestException("id 또는 password가 맞지 않습니다");
        }
        return user;
    }

    public void updatePw(User user, UserUpdatePwDto dto) throws Exception {
        if (!dto.getNewPw().equals(dto.getCheckNewPw())) {
            throw new BadRequestException("비밀번호 재입력이 맞지 않습니다");
        }
        User newUser = userRepository.findById(user.getUsercode()).orElseThrow(
                () -> {throw new NotFoundException("유저를 찾을 수 없습니다");}
        );

        // 새 비밀번호 솔트 값 생성
        String newSalt = createSalt();
        // 새 비밀번호 암호화
        String newPw = encryptPw(newSalt, dto.getNewPw());

        newUser.setPw(newPw);
        newUser.setPwSalt(newSalt);
        userRepository.save(newUser);
    }

    public User updateNickname(User user, UserUpdateNicknameDto dto) throws Exception {
        userRepository.findByNickname(dto.getNewNickname())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 닉네임 입니다");});
        User newUser = userRepository.findById(user.getUsercode()).orElseThrow(
                () -> {throw new NotFoundException("유저를 찾을 수 없습니다");}
        );

        newUser.setNickname(dto.getNewNickname());
        return userRepository.save(newUser);
    }

    private String createSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return HexFormat.of().formatHex(randomBytes);
    }

    private String encryptPw(String salt, String pw) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA3-256");
        messageDigest.update((salt + pw).getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(messageDigest.digest());
    }
}
