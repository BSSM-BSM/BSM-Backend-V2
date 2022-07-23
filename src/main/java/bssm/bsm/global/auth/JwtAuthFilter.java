package bssm.bsm.global.auth;

import bssm.bsm.global.exceptions.NotFoundException;
import bssm.bsm.global.exceptions.UnAuthorizedException;
import bssm.bsm.global.utils.CookieUtil;
import bssm.bsm.global.utils.JwtUtil;
import bssm.bsm.user.entities.User;
import bssm.bsm.user.repositories.UserRepository;
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
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CookieUtil cookieUtil;

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
            setAuthentication(token, req);
        } catch (Exception e) {
            Cookie refreshTokenCookie = cookieUtil.getCookie(req, REFRESH_TOKEN_COOKIE_NAME);
            if (refreshTokenCookie == null) {
                filterChain.doFilter(req, res);
                return;
            }
            try {
                String refreshToken = refreshTokenCookie.getValue();
                User user = userRepository.findById(jwtUtil.getUserCode(refreshToken)).orElseThrow(
                        () -> {throw new NotFoundException("User not found");}
                );
                String newToken = jwtUtil.createAccessToken(user);
                Cookie newTokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, newToken, JWT_TOKEN_MAX_TIME);
                res.addCookie(newTokenCookie);
                setAuthentication(newToken, req);
            } catch (Exception e1) {
                throw new UnAuthorizedException("다시 로그인 해주세요");
            }
        }
        filterChain.doFilter(req, res);
    }

    private void setAuthentication(String token, HttpServletRequest req) throws Exception {
        UserDetails userDetails = new UserInfo(jwtUtil.getUser(token));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

