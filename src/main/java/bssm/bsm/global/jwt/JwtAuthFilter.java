package bssm.bsm.global.jwt;

import bssm.bsm.domain.user.facade.UserFacade;
import bssm.bsm.global.auth.AuthDetailsService;
import bssm.bsm.global.error.exceptions.NotFoundException;
import bssm.bsm.global.error.exceptions.UnAuthorizedException;
import bssm.bsm.global.utils.CookieUtil;
import bssm.bsm.global.jwt.JwtProvider;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.user.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserFacade userFacade;
    private final JwtProvider jwtUtil;
    private final CookieUtil cookieUtil;
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

    private void authentication(String token, HttpServletRequest req) {
        UserDetails userDetails = authDetailsService.loadUserByUsername(String.valueOf(jwtUtil.getUserCode(token)));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void accessTokenCheck(HttpServletRequest req) {
        Cookie tokenCookie = cookieUtil.getCookie(req, TOKEN_COOKIE_NAME);
        String token = tokenCookie.getValue();
        authentication(token, req);
    }

    private void refreshTokenCheck(HttpServletRequest req, HttpServletResponse res) {
        Cookie refreshTokenCookie = cookieUtil.getCookie(req, REFRESH_TOKEN_COOKIE_NAME);
        // 엑세스 토큰 인증에 실패했으면서 리프레시 토큰도 없으면 인증 실패
        if (refreshTokenCookie == null) {
            res.addCookie(cookieUtil.createCookie(TOKEN_COOKIE_NAME, "", 0));
            return;
        }
        try {
            String refreshToken = jwtUtil.getRefreshToken(refreshTokenCookie.getValue());
            // DB에서 사용할 수 있는지 확인
            User user = userFacade.getByAvailableRefreshToken(refreshToken);

            // 새 엑세스 토큰 발급
            String newToken = jwtUtil.createAccessToken(user);
            // 쿠키 생성 및 적용
            Cookie newTokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, newToken, JWT_TOKEN_MAX_TIME);
            res.addCookie(newTokenCookie);

            authentication(newToken, req);
        } catch (Exception e) {
            e.printStackTrace();
            res.addCookie(cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0));
            res.addCookie(cookieUtil.createCookie(TOKEN_COOKIE_NAME, "", 0));
            throw new UnAuthorizedException("다시 로그인 해주세요");
        }
    }

}

