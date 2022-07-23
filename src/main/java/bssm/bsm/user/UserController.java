package bssm.bsm.user;

import bssm.bsm.global.utils.CookieUtil;
import bssm.bsm.global.utils.JwtUtil;
import bssm.bsm.global.utils.UserUtil;
import bssm.bsm.user.dto.request.UserLoginDto;
import bssm.bsm.user.dto.request.UserSignUpDto;
import bssm.bsm.user.dto.response.LoginResponseDto;
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
    @Value("${JWT_TOKEN_MAX_TIME}")
    private long JWT_TOKEN_MAX_TIME;

    @GetMapping()
    public User getUserInfo() {
        return userUtil.getCurrentUser();
    }

    @PostMapping()
    public void signUp(@RequestBody UserSignUpDto dto) throws Exception {
        userService.signUp(dto);
    }

    @PostMapping("login")
    public LoginResponseDto login(@RequestBody UserLoginDto dto , HttpServletResponse res) throws Exception {
        User user = userService.login(dto);

        String token = jwtUtil.createAccessToken(user);
        Cookie tokenCookie = cookieUtil.setCookie(TOKEN_COOKIE_NAME, token, JWT_TOKEN_MAX_TIME);
        res.addCookie(tokenCookie);

        return LoginResponseDto.builder()
                .accessToken(token)
                .build();
    }
}
