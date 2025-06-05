package com.github.vrcxc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.vrcxc.domain.entity.Memos;

public interface MemosMapper extends BaseMapper<Memos> {
    int insertSelective(Memos record);

    int updateByPrimaryKeySelective(Memos record);
}