package bssm.bsm.global.auth;

import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.global.exceptions.UnAuthorizedException;
import bssm.bsm.global.utils.CookieUtil;
import bssm.bsm.global.utils.JwtUtil;
import bssm.bsm.domain.user.entities.User;
import bssm.bsm.domain.user.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Value("${env.cookie.name.token}")
    private String TOKEN_COOKIE_NAME;
    @Value("${env.cookie.name.refreshToken}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${env.jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        Cookie tokenCookie = cookieUtil.getCookie(req, TOKEN_COOKIE_NAME);
        try {
            String token = tokenCookie.getValue();
            authentication(token, req);
        } catch (Exception e) {
            Cookie refreshTokenCookie = cookieUtil.getCookie(req, REFRESH_TOKEN_COOKIE_NAME);
            // 엑세스 토큰 인증에 실패했으면서 리프레시 토큰도 없으면 인증 실패
            if (refreshTokenCookie == null) {
                res.addCookie(cookieUtil.createCookie(TOKEN_COOKIE_NAME, "", 0));
                filterChain.doFilter(req, res);
                return;
            }
            try {
                String refreshToken = refreshTokenCookie.getValue();

                // 리프레시 토큰 만료 기간 확인은 JWT 발급할 때 이미 했으므로 DB에서 사용할 수 있는지 확인
                User user = refreshTokenRepository.findByTokenAndIsAvailable(
                        jwtUtil.getRefreshToken(refreshToken), true
                ).orElseThrow(
                        () -> {throw new NotFoundException("토큰을 찾을 수 없습니다");}
                ).getUser();

                // 새 엑세스 토큰 발급
                String newToken = jwtUtil.createAccessToken(user);
                // 쿠키 생성 및 적용
                Cookie newTokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, newToken, JWT_TOKEN_MAX_TIME);
                res.addCookie(newTokenCookie);

                authentication(newToken, req);
            } catch (Exception e1) {
                e1.printStackTrace();
                res.addCookie(cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0));
                res.addCookie(cookieUtil.createCookie(TOKEN_COOKIE_NAME, "", 0));
                throw new UnAuthorizedException("다시 로그인 해주세요");
            }
        }
        filterChain.doFilter(req, res);
    }

    private void authentication(String token, HttpServletRequest req) {
        UserDetails userDetails = new UserInfo(jwtUtil.getUser(token));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

