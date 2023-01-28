package bssm.bsm.domain.auth.service;

import bssm.bsm.domain.auth.domain.repository.RefreshTokenRepository;
import bssm.bsm.domain.auth.presentation.dto.res.AuthTokenRes;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.global.jwt.JwtProvider;
import bssm.bsm.global.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @Value("${env.cookie.name.token}")
    private String TOKEN_COOKIE_NAME;
    @Value("${env.cookie.name.refreshToken}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${env.jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;
    @Value("${env.jwt.time.refreshToken}")
    private long JWT_REFRESH_TOKEN_MAX_TIME;

    public AuthTokenRes loginPostProcess(HttpServletResponse res, User user) {
        String token = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user.getCode());

        ResponseCookie tokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, token, JWT_TOKEN_MAX_TIME);
        ResponseCookie refreshTokenCookie = cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, JWT_REFRESH_TOKEN_MAX_TIME);
        res.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        res.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return new AuthTokenRes(token, refreshToken);
    }

    @Transactional
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        Cookie refreshTokenCookie = cookieUtil.getCookie(req, REFRESH_TOKEN_COOKIE_NAME);
        expireRefreshToken(refreshTokenCookie);

        res.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0).toString());
        res.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(TOKEN_COOKIE_NAME, "", 0).toString());
    }

    private void expireRefreshToken(Cookie refreshTokenCookie) {
        if (refreshTokenCookie == null) return;

        try {
            String refreshToken = jwtProvider.getRefreshToken(refreshTokenCookie.getValue());
            refreshTokenRepository.findById(refreshToken)
                    .ifPresent(token -> token.setAvailable(false));
        } catch (Exception ignored) {}
    }

}
