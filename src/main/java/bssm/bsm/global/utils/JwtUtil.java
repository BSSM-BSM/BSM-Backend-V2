package bssm.bsm.global.utils;

import bssm.bsm.user.entities.Student;
import bssm.bsm.user.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET_KEY;
    @Value("${JWT_TOKEN_MAX_TIME}")
    private long JWT_TOKEN_MAX_TIME;

    public String createAccessToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("code", user.getUsercode());
        claims.put("level", user.getLevel());
        claims.put("id", user.getId());
        claims.put("nickname", user.getNickname());
        claims.put("uniqNo", user.getUniqNo());
        claims.put("enrolledAt", user.getStudent().getEnrolledAt());
        claims.put("grade", user.getStudent().getGrade());
        claims.put("classNo", user.getStudent().getClassNo());
        claims.put("studentNo", user.getStudent().getStudentNo());
        claims.put("name", user.getStudent().getName());
        return createToken(claims, JWT_TOKEN_MAX_TIME);
    }

    private String createToken(Claims claims, long time) {
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + time))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public User getUser(String token) {
        Claims claims = extractAllClaims(token);
        Student student = Student.builder()
                .enrolledAt(claims.get("enrolledAt", Integer.class))
                .grade(claims.get("grade", Integer.class))
                .classNo(claims.get("classNo", Integer.class))
                .studentNo(claims.get("studentNo", Integer.class))
                .name(claims.get("name", String.class))
                .build();

        return User.builder()
                .usercode(claims.get("code", Integer.class))
                .level(claims.get("level", Integer.class))
                .id(claims.get("id", String.class))
                .nickname(claims.get("nickname", String.class))
                .student(student)
                .uniqNo(claims.get("uniqNo", String.class))
                .build();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
