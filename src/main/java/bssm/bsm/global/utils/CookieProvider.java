package bssm.bsm.global.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class CookieProvider {

    @Value("${env.cookie.domain}")
    private String COOKIE_DOMAIN;
    @Value("${env.cookie.secure}")
    private boolean COOKIE_SECURE;
    @Value("${env.cookie.same-site}")
    private String COOKIE_SAME_SITE;

    public ResponseCookie createCookie(String name, String value, long time) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(COOKIE_SECURE)
                .sameSite(COOKIE_SAME_SITE)
                .path("/")
                .domain(COOKIE_DOMAIN)
                .maxAge(time)
                .build();
    }

    public Cookie findCookie(HttpServletRequest req, String name) {
        final Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }
}
