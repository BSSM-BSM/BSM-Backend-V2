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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final CookieUtil cookieUtil;
    private final UserService userService;
    private final UserUtil userUtil;

    @GetMapping()
    public UserInfoResponse getUserInfo() {
        return userUtil.getUser().toUserInfoResponse();
    }

    @DeleteMapping("logout")
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        userService.logout(req, res);
    }

    @PostMapping("/oauth/bsm")
    public UserLoginResponse bsmOauth(@RequestParam(value = "code") String authCode, HttpServletResponse res) throws Exception {
        return userService.loginPostProcess(res, userService.bsmOauth(authCode));
    }
}
