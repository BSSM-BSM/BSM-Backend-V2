package bssm.bsm.user;

import bssm.bsm.global.utils.CookieUtil;
import bssm.bsm.global.utils.JwtUtil;
import bssm.bsm.global.utils.UserUtil;
import bssm.bsm.user.dto.request.UserLoginDto;
import bssm.bsm.user.dto.request.UserSignUpDto;
import bssm.bsm.user.dto.request.UserUpdateNicknameDto;
import bssm.bsm.user.dto.request.UserUpdatePwDto;
import bssm.bsm.user.dto.response.UserLoginResponseDto;
import bssm.bsm.user.dto.response.UserUpdateNicknameResponseDto;
import bssm.bsm.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final UserService userService;
    private final UserUtil userUtil;

    @Value("${TOKEN_COOKIE_NAME}")
    private String TOKEN_COOKIE_NAME;
    @Value("${REFRESH_TOKEN_COOKIE_NAME}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${JWT_TOKEN_MAX_TIME}")
    private long JWT_TOKEN_MAX_TIME;
    @Value("${JWT_REFRESH_TOKEN_MAX_TIME}")
    private long JWT_REFRESH_TOKEN_MAX_TIME;

    @GetMapping()
    public User getUserInfo() {
        return userUtil.getCurrentUser();
    }

    @DeleteMapping("logout")
    public void logout(HttpServletResponse res) {
        res.addCookie(cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0));
        res.addCookie(cookieUtil.createCookie(TOKEN_COOKIE_NAME, "", 0));
    }

    @PostMapping()
    public void signUp(@RequestBody UserSignUpDto dto) throws Exception {
        userService.signUp(dto);
    }

    @PutMapping("pw")
    public void updatePw(@RequestBody UserUpdatePwDto dto) throws Exception {
        userService.updatePw(userUtil.getCurrentUser(), dto);
    }

    @PutMapping("nickname")
    public UserUpdateNicknameResponseDto updateNickname(@RequestBody UserUpdateNicknameDto dto, HttpServletResponse res) throws Exception {
        User user = userService.updateNickname(userUtil.getCurrentUser(), dto);

        String token = jwtUtil.createAccessToken(user);
        Cookie tokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, token, JWT_TOKEN_MAX_TIME);
        res.addCookie(tokenCookie);

        return new UserUpdateNicknameResponseDto(token);
    }

    @PostMapping("login")
    public UserLoginResponseDto login(@RequestBody UserLoginDto dto, HttpServletResponse res) throws Exception {
        User user = userService.login(dto);

        String token = jwtUtil.createAccessToken(user);
        String refreshToken = jwtUtil.createRefreshToken(user.getUsercode());
        Cookie tokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, token, JWT_TOKEN_MAX_TIME);
        Cookie refreshTokenCookie = cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, JWT_REFRESH_TOKEN_MAX_TIME);
        res.addCookie(tokenCookie);
        res.addCookie(refreshTokenCookie);

        return UserLoginResponseDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
}
