package com.github.vrcxc.utils;

import com.github.vrcxc.domain.User;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.bouncycastle.util.encoders.Base64;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtGenUtil {

    private final JwtCheckUtil jwtCheckUtil;
    private final String privateKey;
    private final Long expiration;
    private final Long refreshExpiration;
    private final RedissonClient redissonClient;

    public JwtGenUtil(JwtCheckUtil jwtCheckUtil, @Value("${jwt.private-key:#{null}}") String privateKey, @Value("${jwt.expiration:300}") Long expiration, @Value("${jwt.refresh-expiration:86400}") Long refreshExpiration, RedissonClient redissonClient) {
        this.jwtCheckUtil = jwtCheckUtil;
        this.privateKey = privateKey;
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
        this.redissonClient = redissonClient;
    }

    private String generateToken(User user, Date expiration, Map<String, Object> claims) {
        PrivateKey privateKey = getPrivateKey();
        return Jwts.builder()
                .subject(user.getUsername())
                .signWith(privateKey)
                .claims(claims)
                .issuedAt(new Date())
                .issuer("vrcxc")
                .expiration(expiration)
                .id(Objects.nonNull(user.getId()) ? user.getId().toString() : null)
                .compact();
    }

    private String generateToken(User user, Long expiration, Map<String, Object> claims) {
        return generateToken(user, new Date(System.currentTimeMillis() + expiration * 1000), claims);
    }

    public String generateToken(User user) {
        return generateToken(user, expiration, Map.of("token-type", "access"));
    }

    public String generateRefreshToken(String username) {
        String refreshToken = generateToken(User.builder().username(username).build(), refreshExpiration, null);
        redissonClient.getBucket("refresh-token:" + refreshToken).set(true, Duration.ofSeconds(refreshExpiration));
        return refreshToken;
    }

    public void generateRefreshToken(String username, HttpServletResponse response) {
        String newRefreshToken = generateRefreshToken(username);
        // 添加 refresh-token 到 cookie
        Cookie cookie = new Cookie("refresh-token", newRefreshToken);
        // 设置 cookie 为 http-only，防止 XSS 攻击
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    public String generateTokenByRefreshToken(User user) {
        return generateToken(user);
    }

    // 使 token 失效
    public void invalidateToken(String token) {
        // 获取 token 的剩余有效时间
        Long remainingTime = jwtCheckUtil.getRemainingTime(token);
        // 将 token 加入黑名单
        redissonClient.getBucket("token:" + token).set(true, Duration.ofMillis(remainingTime));
    }

    // 获取私钥
    private PrivateKey getPrivateKey() {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
