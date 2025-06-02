package com.github.vrcxc.utils;

import com.github.vrcxc.domain.User;
import io.jsonwebtoken.Jwts;
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

    private String generateToken(User user, Date expiration) {
        PrivateKey privateKey = getPrivateKey();
        return Jwts.builder()
                .subject(user.getUsername())
                .signWith(privateKey)
                .issuedAt(new Date())
                .issuer("vrcxc")
                .expiration(expiration)
                .id(Objects.nonNull(user.getId()) ? user.getId().toString() : null)
                .compact();
    }

    private String generateToken(User user, Long expiration) {
        return generateToken(user, new Date(System.currentTimeMillis() + expiration * 1000));
    }

    public String generateToken(User user) {
        return generateToken(user, expiration);
    }

    public String generateRefreshToken(String username) {
        return generateToken(User.builder().username(username).build(), refreshExpiration);
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
