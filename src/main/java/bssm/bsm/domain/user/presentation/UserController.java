package bssm.bsm.domain.user.presentation;

import bssm.bsm.domain.user.presentation.dto.response.UserInfoResponse;
import bssm.bsm.domain.user.service.UserService;
import bssm.bsm.global.utils.CookieUtil;
import bssm.bsm.global.jwt.JwtProvider;
import bssm.bsm.global.utils.UserUtil;
import bssm.bsm.domain.user.presentation.dto.response.UserLoginResponse;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final JwtProvider jwtUtil;
    private final CookieUtil cookieUtil;
    private final UserService userService;
    private final UserUtil userUtil;

    @Value("${env.cookie.name.token}")
    private String TOKEN_COOKIE_NAME;
    @Value("${env.cookie.name.refreshToken}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${env.jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;
    @Value("${env.jwt.time.refreshToken}")
    private long JWT_REFRESH_TOKEN_MAX_TIME;

    @GetMapping()
    public UserInfoResponse getUserInfo() {
        return userUtil.getUser().toUserInfoResponse();
    }

    @DeleteMapping("logout")
    public void logout(HttpServletResponse res) {
        res.addCookie(cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0));
        res.addCookie(cookieUtil.createCookie(TOKEN_COOKIE_NAME, "", 0));
    }

    @PostMapping("/oauth/bsm")
    public UserLoginResponse bsmOauth(@RequestParam(value = "code") String authCode, HttpServletResponse res) throws Exception {
        User user = userService.bsmOauth(authCode);

        String token = jwtUtil.createAccessToken(user);
        String refreshToken = jwtUtil.createRefreshToken(user.getCode());
        Cookie tokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, token, JWT_TOKEN_MAX_TIME);
        Cookie refreshTokenCookie = cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, JWT_REFRESH_TOKEN_MAX_TIME);
        res.addCookie(tokenCookie);
        res.addCookie(refreshTokenCookie);

        return UserLoginResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
}
