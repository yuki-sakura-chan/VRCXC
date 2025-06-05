package com.github.vrcxc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.vrcxc.domain.entity.Memos;
public interface MemosService extends IService<Memos>{


    int insertSelective(Memos record);

    int updateByPrimaryKeySelective(Memos record);

}
