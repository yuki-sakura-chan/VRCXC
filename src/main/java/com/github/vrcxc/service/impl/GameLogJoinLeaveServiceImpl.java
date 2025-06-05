package com.github.vrcxc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.vrcxc.domain.entity.GameLogJoinLeave;
import com.github.vrcxc.mapper.GameLogJoinLeaveMapper;
import com.github.vrcxc.service.GameLogJoinLeaveService;
import org.springframework.stereotype.Service;
@Service
public class GameLogJoinLeaveServiceImpl extends ServiceImpl<GameLogJoinLeaveMapper, GameLogJoinLeave> implements GameLogJoinLeaveService{

    @Override
    public int insertSelective(GameLogJoinLeave record) {
        return baseMapper.insertSelective(record);
    }
    @Override
    public int updateByPrimaryKeySelective(GameLogJoinLeave record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
}
