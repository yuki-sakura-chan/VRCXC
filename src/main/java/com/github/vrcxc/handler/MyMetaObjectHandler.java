package com.github.vrcxc.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.vrcxc.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private final UserUtil userUtil;

    public MyMetaObjectHandler(UserUtil userUtil) {
        this.userUtil = userUtil;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, "creator", Long.class, userUtil.getCurrentUserId());
        strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, "updater", Long.class, userUtil.getCurrentUserId());
        strictInsertFill(metaObject, "deleted", Boolean.class, false);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        strictUpdateFill(metaObject, "updater", Long.class, userUtil.getCurrentUserId());
    }

}
