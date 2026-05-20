package bssm.bsm.global.jwt;

import bssm.bsm.domain.auth.domain.RefreshToken;
import bssm.bsm.domain.user.domain.User;
import bssm.bsm.domain.auth.domain.repository.RefreshTokenRepository;
import bssm.bsm.domain.user.facade.UserFacade;
import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HexFormat;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserFacade userFacade;

    @Value("${jwt.secret-key.auth}")
    private String JWT_AUTH_SECRET_KEY;
    @Value("${jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;
    @Value("${jwt.time.refresh-token}")
    private long JWT_REFRESH_TOKEN_MAX_TIME;

    public String createAccessToken(User user) {
        userFacade.saveUserCache(user);

        Map<String, ?> payload = ImmutableMap.<String, Object>builder()
                .put("id", user.getId())
                .build();
        return createToken(payload, JWT_TOKEN_MAX_TIME);
    }

    public String createRefreshToken(User user) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String newRandomToken = HexFormat.of().formatHex(randomBytes);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(newRandomToken)
                .user(user)
                .isAvailable(true)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        Map<String, ?> payload = ImmutableMap.<String, Object>builder()
                .put("token", newRandomToken)
                .build();
        return createToken(payload, JWT_REFRESH_TOKEN_MAX_TIME);
    }

    private String createToken(Map<String, ?> payload, long time) {
        Date date = new Date();
        return Jwts.builder()
                .claims(payload)
                .issuedAt(date)
                .expiration(new Date(date.getTime() + (time * 1000)))
                .signWith(Keys.hmacShaKeyFor(JWT_AUTH_SECRET_KEY.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }
}
