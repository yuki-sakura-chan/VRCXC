package com.github.vrcxc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.vrcxc.domain.entity.Account;
import com.github.vrcxc.domain.vo.AccountVo;

public interface AccountService extends IService<Account> {

    AccountVo getByPrimaryKey(Long id);

    Boolean register(String username, String password);

    AccountVo getByUsername(String username);

}
