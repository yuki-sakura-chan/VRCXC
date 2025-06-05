package com.github.vrcxc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.vrcxc.domain.entity.Memos;
import com.github.vrcxc.mapper.MemosMapper;
import com.github.vrcxc.service.MemosService;
import org.springframework.stereotype.Service;
@Service
public class MemosServiceImpl extends ServiceImpl<MemosMapper, Memos> implements MemosService{

    @Override
    public int insertSelective(Memos record) {
        return baseMapper.insertSelective(record);
    }
    @Override
    public int updateByPrimaryKeySelective(Memos record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
}
