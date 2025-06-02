package com.github.vrcxc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.vrcxc.domain.entity.Account;
import com.github.vrcxc.domain.vo.AccountVo;
import com.github.vrcxc.exception.BaseException;
import com.github.vrcxc.mapper.AccountMapper;
import com.github.vrcxc.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Override
    public AccountVo getByPrimaryKey(Long id) {
        Account account = Optional.ofNullable(lambdaQuery().eq(Account::getId, id).one()).orElseThrow(() -> new BaseException("Account not found"));
        // 避免返回密码
        account.setPassword(null);
        return account.convert(AccountVo.class);
    }

    @Override
    public Boolean register(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setNickname(username);
        account.setAvatar("default");
        account.setGender(true);
        account.setRegisterIp("127.0.0.1");
        account.setRegisterTime(LocalDateTime.now());
        account.setLastLoginIp("127.0.0.1");
        account.setLastLoginTime(LocalDateTime.now());
        return save(account);
    }


    /**
     * <p>根据用户名获取用户信息</p>
     * <p>如果必须暴露出这个接口，请务必删除密码字段</p>
     * <p>请注意，这个接口会暴露用户是否存在</p>
     * <p>尽量不要外部调用这个接口，最好使用 {@link AccountService#getByPrimaryKey(Long)}</p>
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public AccountVo getByUsername(String username) {
        Account account = lambdaQuery().eq(Account::getUsername, username).one();
        if (Objects.nonNull(account)) {
            return account.convert(AccountVo.class);
        }
        // 注意处理空指针异常，否则会暴露用户是否存在
        return null;
    }

}
