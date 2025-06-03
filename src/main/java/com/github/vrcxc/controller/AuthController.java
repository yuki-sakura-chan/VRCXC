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
        jwtGenUtil.generateRefreshToken(accountVo.getUsername(), response);
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
    public R<String> refreshToken(@CookieValue("refresh-token") String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || jwtCheckUtil.isRefreshTokenExpired(refreshToken)) {
            throw new BaseException("token已过期，请重新登录");
        }
        String username = jwtCheckUtil.extractUsername(refreshToken);
        AccountVo accountVo = Optional.ofNullable(accountService.getByUsername(username)).orElseThrow(() -> new UsernameNotFoundException("账号或密码错误"));
        jwtGenUtil.generateRefreshToken(accountVo.getUsername(), response);
        return R.success(jwtGenUtil.generateTokenByRefreshToken(User.builder().username(accountVo.getUsername()).id(accountVo.getId()).build()));

    }

    /**
     * 目前无法直接验证用户是否为合法的 VRChat 用户，因此只能通过当前系统的账户信息来确保数据的合法性。
     * 为了防止注册接口被滥用，该功能已设置为仅限管理员权限访问。
     * <br>
     * Currently, there is no direct method to verify whether a user is a legitimate VRChat user,
     * so the system relies on the current account information to ensure data validity.
     * To prevent abuse of the registration interface, this feature has been restricted to administrator access only.
     *
     * @param account account
     * @return {@link R}
     */
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

    @GetMapping(value = "/test")
    public R<Boolean> test(){
        return R.success(true);
    }

}
