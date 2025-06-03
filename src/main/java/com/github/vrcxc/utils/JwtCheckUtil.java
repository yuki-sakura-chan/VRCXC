package com.github.vrcxc.utils;

import com.github.vrcxc.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.bouncycastle.util.encoders.Base64;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

@Component
public class JwtCheckUtil {

    private final RedissonClient redissonClient;

    private final String publicKey;

    public JwtCheckUtil(RedissonClient redissonClient, @Value("${jwt.public-key}") String publicKey) {
        this.redissonClient = redissonClient;
        this.publicKey = publicKey;
    }

    public UserDetails getUserDetails(String token) {
        Claims claims = validateToken(token);
        return User.builder()
                .username(claims.getSubject())
                .id(Long.parseLong(claims.getId()))
                .build();
    }

    // 获取 token 的剩余有效时间
    public Long getRemainingTime(String token) {
        return validateToken(token).getExpiration().getTime() - System.currentTimeMillis();
    }

    public Claims validateToken(String token) {
        PublicKey publicKey = getPublicKey();
        return Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getPayload();
    }

    public boolean isTokenExpired(String token) {
        // 检查 token 是否在黑名单
        if (redissonClient.getBucket("token:" + token).isExists()) {
            return true;
        }
        Claims claims = validateToken(token);
        if (!claims.get("token-type").equals("access")){
            return true;
        }
        return claims.getExpiration().before(new Date());
    }

    public boolean isRefreshTokenExpired(String token) {
        // 检查 token 是否在黑名单
        if (redissonClient.getBucket("token:" + token).isExists()) {
            return true;
        }
        // 检查 refresh-token 是否不可用
        if (!redissonClient.getBucket("refresh-token:" + token).isExists()) {
            return true;
        }
        Claims claims = validateToken(token);
        // 删除 refresh-token 可用性，因为 refresh-token 为一次性工具
        redissonClient.getBucket("refresh-token:" + token).delete();
        return claims.getExpiration().before(new Date());
    }

    public String extractUsername(String token) {
        return validateToken(token).getSubject();
    }

    // 获取公钥
    private PublicKey getPublicKey() {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
