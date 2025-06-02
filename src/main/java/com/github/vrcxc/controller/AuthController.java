package com.github.vrcxc.controller;

import com.github.vrcxc.domain.User;
import com.github.vrcxc.domain.dto.AccountDto;
import com.github.vrcxc.domain.vo.AccountVo;
import com.github.vrcxc.exception.BaseException;
import com.github.vrcxc.service.AccountService;
import com.github.vrcxc.utils.JwtCheckUtil;
import com.github.vrcxc.utils.JwtGenUtil;
import com.github.vrcxc.utils.R;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtCheckUtil jwtCheckUtil;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenUtil jwtGenUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtCheckUtil jwtCheckUtil, AccountService accountService, PasswordEncoder passwordEncoder, JwtGenUtil jwtGenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtCheckUtil = jwtCheckUtil;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenUtil = jwtGenUtil;
    }

    @PostMapping(value = "/login")
    public R<String> login(@RequestBody AccountDto account, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword())
        );
        AccountVo accountVo = Optional.ofNullable(accountService.getByUsername(account.getUsername())).orElseThrow(() -> new UsernameNotFoundException("账号或密码错误"));
        String refreshToken = jwtGenUtil.generateRefreshToken(accountVo.getUsername());
        // 添加 refresh-token 到 cookie
        Cookie cookie = new Cookie("refresh-token", refreshToken);
        // 设置 cookie 为 http-only，防止 XSS 攻击
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return R.success(
                jwtGenUtil.generateToken(
                        User.builder()
                                .username(accountVo.getUsername())
                                .id(accountVo.getId())
                                .build()
                )
        );
    }

    @GetMapping(value = "/refresh-token")
    public R<String> refreshToken(@CookieValue("refresh-token") String refreshToken) {
        if (refreshToken == null || jwtCheckUtil.isTokenExpired(refreshToken)) {
            throw new BaseException("token已过期，请重新登录");
        }
        String username = jwtCheckUtil.extractUsername(refreshToken);
        AccountVo accountVo = Optional.ofNullable(accountService.getByUsername(username)).orElseThrow(() -> new UsernameNotFoundException("账号或密码错误"));
        return R.success(jwtGenUtil.generateTokenByRefreshToken(User.builder().username(accountVo.getUsername()).id(accountVo.getId()).build()));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/register")
    public R<Boolean> register(@RequestBody @Validated AccountDto account) {
        String password = passwordEncoder.encode(account.getPassword());
        return R.success(accountService.register(account.getUsername(), password.replace("$", "\\$")));
    }

    @DeleteMapping(value = "/logout")
    public R<Boolean> logout(@CookieValue("refresh-token") String refreshToken, @RequestHeader(value = "Authorization") String token) {
        jwtGenUtil.invalidateToken(token.substring(7));
        jwtGenUtil.invalidateToken(refreshToken);
        return R.success(true);
    }

}
