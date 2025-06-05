package com.github.vrcxc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.vrcxc.domain.entity.GameLogJoinLeave;
public interface GameLogJoinLeaveService extends IService<GameLogJoinLeave>{


    int insertSelective(GameLogJoinLeave record);

    int updateByPrimaryKeySelective(GameLogJoinLeave record);

}
