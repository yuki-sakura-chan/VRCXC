package com.github.vrcxc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.vrcxc.domain.entity.GameLogJoinLeave;

public interface GameLogJoinLeaveMapper extends BaseMapper<GameLogJoinLeave> {
    int insertSelective(GameLogJoinLeave record);

    int updateByPrimaryKeySelective(GameLogJoinLeave record);
}