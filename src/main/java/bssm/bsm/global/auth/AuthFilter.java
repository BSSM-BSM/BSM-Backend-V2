package bssm.bsm.global.auth;

import bssm.bsm.domain.user.facade.UserFacade;
import bssm.bsm.global.error.exceptions.UnAuthorizedException;
import bssm.bsm.global.jwt.JwtProvider;
import bssm.bsm.global.utils.CookieProvider;
import bssm.bsm.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final UserFacade userFacade;
    private final JwtProvider jwtUtil;
    private final CookieProvider cookieProvider;
    private final AuthDetailsService authDetailsService;

    @Value("${env.cookie.name.token}")
    private String TOKEN_COOKIE_NAME;
    @Value("${env.cookie.name.refreshToken}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${env.jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            accessTokenCheck(req);
        } catch (Exception e) {
            refreshTokenCheck(req, res);
        }
        filterChain.doFilter(req, res);
    }

    private void authentication(String token) {
        UserDetails userDetails = authDetailsService.loadUserByUsername(String.valueOf(jwtUtil.getUserCode(token)));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void accessTokenCheck(HttpServletRequest req) {
        Cookie tokenCookie = cookieProvider.findCookie(req, TOKEN_COOKIE_NAME);
        String token = tokenCookie.getValue();
        authentication(token);
    }

    private void refreshTokenCheck(HttpServletRequest req, HttpServletResponse res) {
        Cookie refreshTokenCookie = cookieProvider.findCookie(req, REFRESH_TOKEN_COOKIE_NAME);
        // 엑세스 토큰 인증에 실패했으면서 리프레시 토큰도 없으면 인증 실패
        if (refreshTokenCookie == null) {
            res.addHeader(HttpHeaders.SET_COOKIE, cookieProvider.createCookie(TOKEN_COOKIE_NAME, "", 0).toString());
            return;
        }
        try {
            String refreshToken = jwtUtil.getRefreshToken(refreshTokenCookie.getValue());
            // DB에서 사용할 수 있는지 확인
            User user = userFacade.findByRefreshToken(refreshToken);
            // 새 엑세스 토큰 발급
            String newToken = jwtUtil.createAccessToken(user);
            // 쿠키 생성 및 적용
            ResponseCookie newTokenCookie = cookieProvider.createCookie(TOKEN_COOKIE_NAME, newToken, JWT_TOKEN_MAX_TIME);
            res.addHeader(HttpHeaders.SET_COOKIE, newTokenCookie.toString());

            authentication(newToken);
        } catch (Exception e) {
            e.printStackTrace();
            res.addHeader(HttpHeaders.SET_COOKIE, cookieProvider.createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0).toString());
            res.addHeader(HttpHeaders.SET_COOKIE, cookieProvider.createCookie(TOKEN_COOKIE_NAME, "", 0).toString());
            throw new UnAuthorizedException("다시 로그인 해주세요");
        }
    }

}

