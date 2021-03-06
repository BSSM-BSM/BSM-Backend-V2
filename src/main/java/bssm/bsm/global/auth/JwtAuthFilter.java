package bssm.bsm.global.auth;

import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.global.exceptions.UnAuthorizedException;
import bssm.bsm.global.utils.CookieUtil;
import bssm.bsm.global.utils.JwtUtil;
import bssm.bsm.user.entities.User;
import bssm.bsm.user.repositories.RefreshTokenRepository;
import bssm.bsm.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Value("${TOKEN_COOKIE_NAME}")
    private String TOKEN_COOKIE_NAME;
    @Value("${REFRESH_TOKEN_COOKIE_NAME}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${JWT_TOKEN_MAX_TIME}")
    private long JWT_TOKEN_MAX_TIME;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        Cookie tokenCookie = cookieUtil.getCookie(req, TOKEN_COOKIE_NAME);
        try {
            String token = tokenCookie.getValue();
            authentication(token, req);
        } catch (Exception e) {
            Cookie refreshTokenCookie = cookieUtil.getCookie(req, REFRESH_TOKEN_COOKIE_NAME);
            // ????????? ?????? ????????? ?????????????????? ???????????? ????????? ????????? ?????? ??????
            if (refreshTokenCookie == null) {
                filterChain.doFilter(req, res);
                return;
            }
            try {
                String refreshToken = refreshTokenCookie.getValue();

                // ???????????? ?????? ?????? ?????? ????????? JWT ????????? ??? ?????? ???????????? DB?????? ????????? ??? ????????? ??????
                User user = refreshTokenRepository.findByTokenAndIsAvailable(
                        jwtUtil.getRefreshToken(refreshToken), true
                ).orElseThrow(
                        () -> {throw new NotFoundException("????????? ?????? ??? ????????????");}
                ).getUser();

                // ??? ????????? ?????? ??????
                String newToken = jwtUtil.createAccessToken(user);
                // ?????? ?????? ??? ??????
                Cookie newTokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, newToken, JWT_TOKEN_MAX_TIME);
                res.addCookie(newTokenCookie);

                authentication(newToken, req);
            } catch (NotFoundException e1) {
              throw e1;
            } catch (Exception e1) {
                e1.printStackTrace();
                throw new UnAuthorizedException("?????? ????????? ????????????");
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

